package com.nju.android.health.views.activities.next;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.nju.android.health.R;

public class SearchResultDetailActivity extends AppCompatActivity {

    private TextView view1;
    private TextView view2;
    private TextView view3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_detail);

        initView();


        setText();
    }


    private void initView() {
        view1 = (TextView) findViewById(R.id.search_result_detail_explain);
        view2 = (TextView) findViewById(R.id.search_result_detail_reason);
        view3 = (TextView) findViewById(R.id.search_result_detail_suggestion);
    }

    private void setText() {
        view1.setText("偏头痛（migraine）是临床最常见的原发性头痛类型，临床以发作性中重度、搏动样头痛为主要表现，头痛多为偏侧，一般持续4～72小时，可伴有恶心、呕吐，光、声刺激或日常活动均可加重头痛，安静环境、休息可缓解头痛。偏头痛是一种常见的慢性神经血管性疾患，多起病于儿童和青春期，中青年期达发病高峰，女性多见，男女患者比例约为1∶2～3，人群中患病率为5%～10%，常有遗传背景。\n");
        view2.setText("遗传因素\n\n" +
                "约60%的偏头痛病人有家族史，其亲属出现偏头痛的风险是一般人群的3～6倍，家族性偏头痛患者尚未发现一致的孟德尔遗传规律，反映了不同外显率及多基因遗传特征与环境因素的相互作用。家族性偏瘫型偏头痛是明确的有高度异常外显率的常染色体显性遗传，已定位在19p13（与脑部表达的电压门P/Q钙通道基因错译突变有关）、1q21和1q31等三个疾病基因位点。\n\n" +
                "内分泌和代谢因素\n\n" +
                "本病女性多于男性，多在青春期发病，月经期容易发作，妊娠期或绝经后发作减少或停止。这提示内分泌和代谢因素参与偏头痛的发病。此外，5-羟色胺（5-HT）、去甲肾上腺素、P物质和花生四烯酸等代谢异常也可影响偏头痛发生。\n\n" +
                "饮食与精神因素\n\n" +
                "偏头痛发作可由某些食物和药物诱发，食物包括含酪胺的奶酪、含亚硝酸盐防腐剂的肉类和腌制食品、含苯乙胺的巧克力、食品添加剂如谷氨酸钠（味精），红酒及葡萄酒等。药物包括口服避孕药和血管扩张剂如硝酸甘油等。 另外一些环境和精神因素如紧张、过劳、情绪激动、睡眠过度或过少、月经、强光 也可诱发。\n");
        view3.setText("远离酪胺酸类食物\n\n" +
                "酪胺酸是造成血管痉挛的主要诱因易导致头痛发作，这类食物包括：奶酪 、巧克力、柑橘类食物，以及腌渍沙丁鱼、鸡肝、西红柿、牛奶、乳酸饮料等。\n\n" +
                "减少摄酒\n\n" +
                "所有酒精类饮料都会引发头痛，特别是红酒含有更多诱发头痛的化学物质。如果一定要喝，最好选择伏特加、白酒这类无色酒。\n\n" +
                "学会减压\n\n" +
                "放松心情，选择泡泡温水浴，做瑜伽等放松运动可以避免头痛。\n\n" +
                "规律运动\n\n" +
                "对有偏头痛的人来说，着重呼吸训练、调息的运动（例如瑜伽、气功），可帮助患者稳定自律神经系统、减缓焦虑、肌肉紧绷等症状。\n\n" +
                "生活规律\n\n" +
                "营造安静的环境，维持规律的作息，即使在假日也定时上床、起床。");
    }


}
