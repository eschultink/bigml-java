package org.bigml.binding;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import cucumber.annotation.en.Given;

public class CompositesStepdefs {

    // Logging
    Logger logger = LoggerFactory.getLogger(CompositesStepdefs.class);
    
    @Autowired
    CommonStepdefs commonSteps;

    @Autowired
    private ContextRepository context;

    @Given("^I create an optiml from a dataset$")
    public void I_create_an_optiml_from_a_dataset() throws Throwable {
        String datasetId = (String) context.dataset.get("resource");

        JSONObject args = new JSONObject();
        args.put("tags", Arrays.asList("unitTest"));
        args.put("max_training_time", 1000);
        args.put("model_types", Arrays.asList("model", "logisticregression"));
        args.put("metric", "max_phi");
        args.put("number_of_model_candidates", 4);

        JSONObject resource = BigMLClient.getInstance().createOptiML(
                datasetId, args, 5, null);
        context.status = (Integer) resource.get("code");
        context.location = (String) resource.get("location");
        context.optiML = (JSONObject) resource.get("object");
        commonSteps.the_resource_has_been_created_with_status(context.status);
    }
    
    @Given("^I create a fusion from models$")
    public void I_create_a_fusion_from_models() throws Throwable {
        List models = new ArrayList();
        for (int iModel = 0; iModel < context.models.size(); iModel++ ) {
            JSONObject modelInList = (JSONObject) context.models.get(iModel);
            models.add(modelInList.get("resource"));
        }
        
        JSONObject args = new JSONObject();
        args.put("tags", Arrays.asList("unitTest"));
        
        JSONObject resource = BigMLClient.getInstance().createFusion(
        		models, args, 5, null);
        context.status = (Integer) resource.get("code");
        context.location = (String) resource.get("location");
        context.fusion = (JSONObject) resource.get("object");
        commonSteps.the_resource_has_been_created_with_status(context.status);
    }

}