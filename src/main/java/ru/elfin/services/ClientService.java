package ru.elfin.services;

import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.elfin.models.ClientAssessment;
import ru.elfin.repositories.ClientAssessmentRepository;
import ru.elfin.requests.ClientDataScoringDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClientService {

    private static final String DECISION_KEY = "Decision_1";
    private static final String DIAGRAM_PATH = "/dmn/diagram_2.dmn";
    private static final String RESIDENT_CODE = "9909";
    private static final String REGION_CODE = "24";
    private static final String DMN_STOP_FACTOR_RESULT = "stopFactorResult";
    private static final String DMN_DESCRIPTION = "description";

    private final ClientAssessmentRepository clientAssessmentRepository;
    private final DmnEngine dmnEngine;

    @Autowired
    public ClientService(ClientAssessmentRepository clientAssessmentRepository, DmnEngine dmnEngine) {
        this.clientAssessmentRepository = clientAssessmentRepository;
        this.dmnEngine = dmnEngine;
    }

    public ClientAssessment assessClient(ClientDataScoringDto clientDataScoringDto) {
        DmnDecision decision = dmnEngine.parseDecision(DECISION_KEY, getClass().getResourceAsStream(DIAGRAM_PATH));
        DmnDecisionTableResult assessResult = dmnEngine.evaluateDecisionTable(decision, buildRequest(clientDataScoringDto));

        boolean res = (boolean) assessResult.getFirstResult().get(DMN_STOP_FACTOR_RESULT);

        ClientAssessment clientAssessment = new ClientAssessment();
        clientAssessment.setResult(res);
        clientAssessment.setDetails(buildAssessmentDetails(assessResult.getResultList()));

        clientAssessmentRepository.save(clientAssessment);

        return clientAssessment;
    }

    /**
     * Формирование полей для запроса в камунду.
     */
    private Map<String, Object> buildRequest(ClientDataScoringDto clientDataScoringDto) {
        Map<String, Object> requestMap = new HashMap<>();

        if (clientDataScoringDto.getInn().length() == 12) {
            requestMap.put("physicalPerson", true);
        }

        if(clientDataScoringDto.getInn().startsWith(REGION_CODE)) {
            requestMap.put("region", true);
        }

        if(clientDataScoringDto.getInn().startsWith(RESIDENT_CODE)) {
            requestMap.put("resident", true);
        }

        requestMap.put("capital", clientDataScoringDto.getCapital());

        return requestMap;
    }

    /**
     * Формирование описания оценки.
     */
    private String buildAssessmentDetails( List<Map<String, Object>> result) {
        String assessmentDetails = "";
        for (Map<String, Object> stringObjectMap : result) {
            assessmentDetails += String.format("%s - %s\n", stringObjectMap.get(DMN_DESCRIPTION), stringObjectMap.get(DMN_STOP_FACTOR_RESULT));
        }
        return assessmentDetails;
    }
}
