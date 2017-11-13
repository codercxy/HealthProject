package com.nju.android.health.views.activities.next;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nju.android.health.R;
import com.nju.android.health.utils.FlowLayout;
import com.nju.android.health.utils.MyProgressBar;
import com.nju.android.health.utils.RecyclerItemClickListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smile.Network;
import smile.SMILEException;
import smile.ValueOfInfo;


public class SearchResultActivity extends AppCompatActivity implements View.OnClickListener{

    private String DISEASE = "disease";
    private String SYMPTOM = "sympton";

    private static Double[][] sex_age = {{0.0, 4.76, 9.45, 17.27, 27.24, 40.79, 52.46},
            {0.0, 2.13, 3.82, 11.88, 28.42, 43.66, 55.70}};
    private static Double[] alcohol = {0.0, 24.04, 23.65, 26.25, 30.20, 35.22};
    private static Double[] weightIndex = {0.0, 13.7, 16.5, 33.3, 51.2};
    private static Double[] histroy = {0.0, 18.22, 30.38};
    private static Double[] chol = {0.0, 21.29, 43.26};
    private static Double[] smoke = {0.0, 22.54, 26.32};
    private static Double[] bloodSugar = {0.0, 22.35, 64.29};


    private FlowLayout flowLayout;
    private ArrayList<MyProgressBar> progressBarList;
    private MyProgressBar headProgressBar;
    /*private MyProgressBar brainProgressBar;
    private MyProgressBar nextProgressBar;*/
    private Context context;

    private LinearLayout ll_headache;
    private LinearLayout ll_brain;
    private LinearLayout ll_braing_next;

    //nodeData
    private String[] symptoms;
    private String[] symptomsName;
    private String[] diseases;
    private ArrayList<double[]> nodeDef = new ArrayList<>();
    private static final String True = "True";
    private static final String False = "False";
    private Map<String, Boolean> symptomParams = new HashMap<String, Boolean>();

    Network net = new Network();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        context = SearchResultActivity.this;



        initView();

        initData();

//        CreateNetwork();
//        InfereceWithBayesianNetwork();
//        UpgradeToInfluenceDiagram();
//        InferenceWithInfluenceDiagram();
//        ComputeValueOfInformation();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.search_result_headache:
                intent.putExtra("extra_id", 0);

                break;
            /*case R.id.search_result_brain:
                intent.putExtra("extra_id", 1);
                break;
            case R.id.search_result_brain_next:
                intent.putExtra("extra_id", 2);
                break;*/

            default:
                break;
        }
        intent.setClass(SearchResultActivity.this, SearchResultDetailActivity.class);
        startActivity(intent);

    }

    private void initView() {
        flowLayout = (FlowLayout) findViewById(R.id.search_result_flow);

        headProgressBar = (MyProgressBar) findViewById(R.id.search_result_headache_bar);
        /*brainProgressBar = (MyProgressBar) findViewById(R.id.search_result_brain_bar);
        nextProgressBar = (MyProgressBar) findViewById(R.id.search_result_brain_next_bar);*/

        ll_headache = (LinearLayout) findViewById(R.id.search_result_headache);
        /*ll_brain = (LinearLayout) findViewById(R.id.search_result_brain);
        ll_braing_next = (LinearLayout) findViewById(R.id.search_result_brain_next);*/

        ll_headache.setOnClickListener(this);
        /*ll_brain.setOnClickListener(this);
        ll_braing_next.setOnClickListener(this);*/

    }

    private void initData() {
        flowLayout = (FlowLayout) findViewById(R.id.search_result_flow);
//        TextView view = (TextView) flowLayout.getChildAt(0);
//        view.setBackground(this.getResources().getDrawable(R.drawable.flow_flag_2));
//        view.setTextColor(this.getResources().getColor(R.color.search_white));

        progressBarList = new ArrayList<MyProgressBar>();
        progressBarList.add(headProgressBar);
        /*progressBarList.add(brainProgressBar);
        progressBarList.add(nextProgressBar);*/
        //初始化node data
        getData();

        CreateNetwork();
//        InfereceWithBayesianNetwork();

        addView();

    }

    private void getData() {
//        diseases = new String[]{"头痛", "偏头痛", "脑肿瘤", "脑膜炎"};
//
        symptomsName = new String[]{"头痛", "晕眩", "鼻出血"};

        diseases = new String[]{"a"};

        symptoms = new String[]{"q", "w", "e"};

        for (int i = 0; i< symptoms.length; i++) {
            symptomParams.put(symptoms[i], false);
        }

        int[] index = {1, 1, 1, 1, 1, 1, 1, 1};
        //高血压
        nodeDef.add(new double[]{calPressure(index), 1 - calPressure(index)});

        //头痛
        nodeDef.add(new double[]{0.8, 0.2, 0.5, 0.5});
        //眩晕
        nodeDef.add(new double[]{0.8, 0.2, 0.3, 0.7});
        //鼻出血
        nodeDef.add(new double[]{0.8, 0.2, 0.2, 0.8});

        /*
        //头痛
        nodeDef.add(new double[]{0.3, 0.7});
        //偏头痛
        nodeDef.add(new double[]{0.6, 0.4, 0.01, 0.99});
        //脑肿瘤
        nodeDef.add(new double[]{0.2, 0.8, 0.01, 0.99});
        //脑膜炎
        nodeDef.add(new double[]{0.2, 0.8, 0.01, 0.99});


        //抽搐
        nodeDef.add(new double[]{0.8, 0.2, 0.01, 0.99});
        //恶心
        nodeDef.add(new double[]{0.9, 0.1, 0.8, 0.2, 0.8, 0.2, 0.1, 0.9});
        //钝痛
        nodeDef.add(new double[]{0.3, 0.7, 0.1, 0.9});
        //发烧
        nodeDef.add(new double[]{0.8, 0.2, 0.1, 0.9});*/

    }
    private double calPressure(int[] index) {
        double result = 1.0;
        result = (1 - sex_age[index[0]][index[1]] / 100) *
                (1 - weightIndex[index[2]] / 100) *
                (1 - histroy[index[3]] / 100) *
                (1 - chol[index[4]] / 100) *
                (1 - smoke[index[5]] / 100) *
                (1 - bloodSugar[index[7]] / 100);
        return 1 - result;
    }

    private void addView() {
        for (int i = 0; i < symptoms.length; i++) {
            flowLayout.addView(setUpView(symptomsName[i], i), i);
        }

    }

    private TextView setUpView(String text, final int index) {
        final TextView newView = new TextView(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(params);
        marginLayoutParams.setMargins(16, 16, 16, 16);
        newView.setLayoutParams(marginLayoutParams);
//        newView.setTextAppearance();.

//        newView.setGravity(Gravity.CENTER);

        newView.setTextAppearance(context, R.style.text_flag);
        newView.setText(text);
        newView.setBackground(context.getResources().getDrawable(R.drawable.flow_flag_1));
        newView.setTextColor(context.getResources().getColor(R.color.search_blue));
        newView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                symptomParams.put(symptoms[index], !symptomParams.get(symptoms[index]));

                if(newView.getBackground().getConstantState().equals(context.getResources().getDrawable(R.drawable.flow_flag_2).getConstantState())){
                    newView.setBackground(context.getResources().getDrawable(R.drawable.flow_flag_1));
                    newView.setTextColor(context.getResources().getColor(R.color.search_blue));
                } else {
                    newView.setBackground(context.getResources().getDrawable(R.drawable.flow_flag_2));
                    newView.setTextColor(context.getResources().getColor(R.color.search_white));
                }

                updateProgressBar();

            }
        });

        return newView;
    }

    private void updateProgressBar() {
        //deal with head

        // Updating the network:
//        File fileDir = getApplicationContext().getFilesDir();
//        net.readFile(fileDir + "/bayesNet.xdsl");
//        net.updateBeliefs();
        for (int i = 0; i < diseases.length; i++) {
//            System.out.println(diseases[i + 1]);
            updateProgressBarDetail(diseases[i], progressBarList.get(i));
//            testUpdate(diseases[i + 1], progressBarList.get(i), i + 1);
        }
    }




    private void updateProgressBarDetail(String disease, MyProgressBar progressBar) {
        try {
            net.clearAllEvidence();

            //setEvidence
            for (int i = 0; i < symptoms.length; i++) {
                if (symptomParams.get(symptoms[i])) {
                    net.setEvidence(symptoms[i], True);

//                System.out.println(net.getEvidence(symptoms[i]));
                } else {
                    net.setEvidence(symptoms[i], False);
//                System.out.println(net.getEvidence(symptoms[i]));
//                System.out.println("false");
                }
            }

            for (int i  = 0; i < symptoms.length; i++) {
                System.out.println(symptoms[i] + ":");
                System.out.println(net.getEvidence(symptoms[i]));
            }

            net.updateBeliefs();

            net.getNode(disease);

            String[] outcomeIds = net.getOutcomeIds(disease);


            int outcomeIndex;
            double[] aValues;
            for (outcomeIndex = 0; outcomeIndex < outcomeIds.length; outcomeIndex++) {
//                System.out.println(outcomeIds[outcomeIndex]);
                if ("True".equals(outcomeIds[outcomeIndex]))
                    break;
            }
            // Getting the value of the probability:
            aValues = net.getNodeValue(disease);
            double P_SuccIsFailGivenForeIsGood = aValues[outcomeIndex];

            progressBar.setProgress((int) (P_SuccIsFailGivenForeIsGood * 100));

        }
        catch (SMILEException e) {
            System.out.println(e.getMessage());
        }
    }

    public void CreateNetwork() {
        try {
            // Creating nodes and setting/adding outcomes:
            for (int i = 0; i < diseases.length; i++) {
                net.addNode(Network.NodeType.Cpt, diseases[i]);
                net.setOutcomeId(diseases[i], 0, True);
                net.setOutcomeId(diseases[i], 1, False);
            }
            for (int i = 0; i < symptoms.length; i++) {
                net.addNode(Network.NodeType.Cpt, symptoms[i]);
                net.setOutcomeId(symptoms[i], 0, True);
                net.setOutcomeId(symptoms[i], 1, False);
            }

            // Adding arcs
            /*net.addArc(diseases[0], diseases[1]);
            net.addArc(diseases[0], diseases[2]);
            net.addArc(diseases[0], diseases[3]);

            net.addArc(diseases[0], symptoms[0]);
            net.addArc(diseases[1], symptoms[1]);
            net.addArc(diseases[2], symptoms[1]);
            net.addArc(diseases[2], symptoms[2]);
            net.addArc(diseases[3], symptoms[3]);*/
            net.addArc(diseases[0], symptoms[0]);
            net.addArc(diseases[0], symptoms[1]);
            net.addArc(diseases[0], symptoms[2]);

            for (int i = 0; i < symptoms.length + diseases.length; i++) {
                if (i < diseases.length)
                    net.setNodeDefinition(diseases[i], nodeDef.get(i));
                else
                    net.setNodeDefinition(symptoms[i - diseases.length], nodeDef.get(i));
            }


            // Changing the nodes' spacial and visual attributes:
//            net.setNodePosition("Success", 20, 20, 80, 30);
////            net.setNodeBgColor("Success", Color.red);
////            net.setNodeTextColor("Success", Color.white);
////            net.setNodeBorderColor("Success", Color.black);
//            net.setNodeBorderWidth("Success", 2);
//            net.setNodePosition("Forecast", 30, 100, 60, 30);

            // Writting the network to a file:
            File fileDir = getApplicationContext().getFilesDir();
            net.writeFile(fileDir + "/bayesNet.xdsl");

        }
        catch (SMILEException e) {
            System.out.println(e.getMessage());
        }
    }

    public void InfereceWithBayesianNetwork() {
        try {
            File fileDir = getApplicationContext().getFilesDir();
            net.readFile(fileDir + "/bayesNet.xdsl");

            // ---- We want to compute P("Forecast" = Moderate) ----
            // Updating the network:
            net.updateBeliefs();


            // Getting the handle of the node "Forecast":
            net.getNode("Forecast");

            // Getting the index of the "Moderate" outcome:
            String[] aForecastOutcomeIds = net.getOutcomeIds("Forecast");
            int outcomeIndex;
            for (outcomeIndex = 0; outcomeIndex < aForecastOutcomeIds.length; outcomeIndex++)
                if ("Moderate".equals(aForecastOutcomeIds[outcomeIndex]))
                    break;

            // Getting the value of the probability:
            double[] aValues = net.getNodeValue("Forecast");
            double P_ForecastIsModerate = aValues[outcomeIndex];

            System.out.println("P(\"Symptom\" = Moderate) = "      + P_ForecastIsModerate);


            // ---- We want to compute P("Success" = Failure | "Forecast" = Good) ----
            // Introducing the evidence in node "Forecast":
            net.setEvidence("Forecast", "Good");

            // Updating the network:
            net.updateBeliefs();

            // Getting the handle of the node "Success":
            net.getNode("Success");

            // Getting the index of the "Failure" outcome:
            String[] aSuccessOutcomeIds = net.getOutcomeIds("Success");
            for (outcomeIndex = 0; outcomeIndex < aSuccessOutcomeIds.length; outcomeIndex++)
                if ("Failure".equals(aSuccessOutcomeIds[outcomeIndex]))
                    break;

            // Getting the value of the probability:
            aValues = net.getNodeValue("Success");
            double P_SuccIsFailGivenForeIsGood = aValues[outcomeIndex];

            System.out.println("P(\"Disease\" = Failure | \"Symptom\" = Good) = " + P_SuccIsFailGivenForeIsGood);

            // ---- We want to compute P("Success" = Success | "Forecast" = Poor) ----
            // Clearing the evidence in node "Forecast":
            net.clearEvidence("Forecast");

            // Introducing the evidence in node "Forecast":
            net.setEvidence("Forecast", "Good");

            // Updating the network:
            net.updateBeliefs();

            // Getting the index of the "Failure" outcome:
            aSuccessOutcomeIds = net.getOutcomeIds("Success");
            for (outcomeIndex = 0; outcomeIndex < aSuccessOutcomeIds.length; outcomeIndex++)
                if ("Failure".equals(aSuccessOutcomeIds[outcomeIndex]))
                    break;

            // Getting the value of the probability:
            aValues = net.getNodeValue("Success");
            double P_SuccIsSuccGivenForeIsPoor = aValues[outcomeIndex];

            System.out.println("P(\"Disease\" = Success | \"Symptom\" = Poor) = " + P_SuccIsSuccGivenForeIsPoor);
        }
        catch (SMILEException e) {
            System.out.println(e.getMessage());
        }
    }
    public void UpgradeToInfluenceDiagram() {
        try {
            Network net = new Network();
            net.readFile("tutorial_a.xdsl");

            // Creating node "Invest" and setting/adding outcomes:
            net.addNode(Network.NodeType.List, "Invest");
            net.setOutcomeId("Invest", 0, "Invest");
            net.setOutcomeId("Invest", 1, "DoNotInvest");

            // Creating the value node "Gain":
            net.addNode(Network.NodeType.Table, "Gain");

            // Adding an arc from "Invest" to "Gain":
            net.addArc("Invest", "Gain");

            // Adding an arc from "Success" to "Gain":
            net.getNode("Success");
            net.addArc("Success", "Gain");

            // Filling in the utilities for the node "Gain". The utilities are:
            // U("Invest" = Invest, "Success" = Success) = 10000
            // U("Invest" = Invest, "Success" = Failure) = -5000
            // U("Invest" = DoNotInvest, "Success" = Success) = 500
            // U("Invest" = DoNotInvest, "Success" = Failure) = 500
            double[] aGainDef = {10000, -5000, 500, 500};
            net.setNodeDefinition("Gain", aGainDef);
            File fileDir = getApplicationContext().getFilesDir();
            net.writeFile(fileDir + "/tutorial_b.xdsl");
        }
        catch (SMILEException e) {
            System.out.println(e.getMessage());
        }
    }
    public void InferenceWithInfluenceDiagram() {
        try {
            // Loading and updating the influence diagram:
            Network net = new Network();
            net.readFile("tutorial_b.xdsl");
            net.updateBeliefs();

            // Getting the utility node's handle:
            net.getNode("Gain");

            // Getting the handle and the name of value indexing parent (decision node):
            int[] aValueIndexingParents = net.getValueIndexingParents("Gain");
            int nodeDecision = aValueIndexingParents[0];
            String decisionName = net.getNodeName(nodeDecision);

            // Displaying the possible expected values:
            System.out.println("These are the expected utilities:");
            for (int i = 0; i < net.getOutcomeCount(nodeDecision); i++) {
                String parentOutcomeId = net.getOutcomeId(nodeDecision, i);
                double expectedUtility = net.getNodeValue("Gain")[i];

                System.out.print("  - \"" + decisionName + "\" = " + parentOutcomeId + ": ");
                System.out.println("Expected Utility = " + expectedUtility);
            }
        }
        catch (SMILEException e) {
            System.out.println(e.getMessage());
        }
    }

    public void ComputeValueOfInformation() {
        try {
            Network net = new Network();
            net.readFile("tutorial_b.xdsl");

            ValueOfInfo voi = new ValueOfInfo(net);

            // Getting the handles of nodes "Forecast" and "Invest":
            net.getNode("Forecast");
            net.getNode("Invest");

            voi.addNode("Forecast");
            voi.setDecision("Invest");
            voi.update();

            double[] results = voi.getValues();
            double EVIForecast = results[0];

            System.out.println("Expected Value of Information (\"Forecast\") = " + EVIForecast);

        }
        catch (SMILEException e) {
            System.out.println(e.getMessage());
        }
    }

}
