package com.nju.android.health.views.fragments.health;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nju.android.health.R;
import com.nju.android.health.model.data.Article;
import com.nju.android.health.utils.RecyclerInsetsDecoration;
import com.nju.android.health.utils.RecyclerViewClickListener;
import com.nju.android.health.views.activities.health.ArticleActivity;
import com.nju.android.health.views.adapters.HealthAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class HealthFragment extends Fragment implements RecyclerViewClickListener{
    /*implements OnPageChangeListener,*/
    /*private ViewPager viewPager;
    private ViewGroup viewGroup;

    private ImageView[] tips;

    private ImageView[] mImageViews;

    private int[] imgIdArray;*/

    private SwipeRefreshLayout mRefreshSrl;
    private RecyclerView mRecycler;
    private HealthAdapter mAdapter;
    private List<Article> articles;



    public HealthFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_health, container, false);

        initView(view);

        initData();

        initRecycler();



        return view;
    }

    private void initView(View view) {
        /*viewGroup = (ViewGroup) view.findViewById(R.id.health_banner_group);
        viewPager = (ViewPager) view.findViewById(R.id.health_banner_pager);*/

        mRecycler = (RecyclerView) view.findViewById(R.id.health_recycler);
        mRefreshSrl = (SwipeRefreshLayout) view.findViewById(R.id.health_refresh);
    }

    private void initData() {
        /*imgIdArray = new int[] {R.drawable.health1, R.drawable.health2, R.drawable.health3};

        tips = new ImageView[imgIdArray.length];
        for (int i = 0; i < tips.length; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));

            tips[i] = imageView;
            if (i == 0) {
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            layoutParams.leftMargin = 5;
            layoutParams.rightMargin = 5;
            viewGroup.addView(imageView, layoutParams);
        }
        initImg();

        setAdapter();*/

        articles = new ArrayList<Article>();
        Article article = new Article();
        article.setImgRes(R.drawable.health);
        article.setTitle("跑步可能会伤膝盖，但不运动更伤膝");
        article.setType("减肥");
        article.setAuthor("佚名");
        article.setDate("2016年10月1日");
        article.setContent("一、关节软骨是运动的保护神\n" +
                "\n" +
                "关节表面覆盖有一层薄薄的软骨，正常情况下软骨表面光滑（相邻软骨面的摩擦系数比两块冰面还要小）" +
                "，呈淡蓝色，有光泽，还有一定弹性，当软骨受到压力作用时，还可以产生少许变形。人在运动时，" +
                "就会对关节产生摩擦、挤压、冲击和负载，而关节软骨具有减小摩擦、缓冲冲击、分散压力、吸收震动等重要作用。" +
                "因此，关节软骨是运动的保护神。\n" +
                "\n" +
                "二、运动是关节软骨的营养师\n" +
                "\n" +
                "关节软骨没有神经支配，也没有血管，其营养成分必须从关节滑液中获取。当关节软骨受压时，软骨变薄，" +
                "滑液从软骨中被挤出，而压力消失时，滑液被吸入软骨中，这就如同海绵效应，就在这一挤一吸中实现了对于软骨的营养。" +
                "因此，关节软骨的营养代谢必须通过关节运动才能实现，只有让关节软骨不断受到适度压力刺激，才能维持软骨的正常结构和功能。\n" +
                "\n" +
                "三、为什么久坐不动更伤膝\n" +
                "\n" +
                "既然关节表面软骨的营养依赖于运动，那么久坐不动，会对软骨乃至关节健康产生哪些负面影响呢？首先，如果缺乏运动，" +
                "那么就丧失了运动对于软骨的挤压作用，关节滑液进出关节软骨减少，那么软骨的正常新陈代谢就会受阻，关节软骨同样还是会像过度运动那样发生退变。\n");

        Article article2 = new Article();
        article2.setImgRes(R.drawable.article_pres);
        article2.setTitle("如何在家测血压？");
        article2.setType("血压");
        article2.setAuthor("Luxenius");
        article2.setDate("2014年6月8日");
        article2.setContent("一、坐着量？躺着量？\n" +
                "\n" +
                "高血压在普通人中已经极为普遍，中国10个人中有3个人患有高血压病，老年人中高血压患者更是超过一半，而且其中大部分并不自知。 \n" +
                "\n" +
                "高血压是长期危害生命和生存质量的慢性病，以正常血压115/75 mmHg为起点，长期血压每上升20 mmHg，死亡风险就会翻一番。 \n" +
                "\n" +
                "在很多情况下，我们不仅需要在医院看病时测血压，还需要在家里为自己、为家人进行血压测量。 \n" +
                "\n" +
                "研究表明，教会有高血压亲人监测血压并养成习惯，能大大增强他们对治疗的依从性，改善慢性病的治疗效果，最终改善远期的生存率。今天我们就来谈谈家庭血压监测。\n" +
                "\n" +
                "哪些人需要在家监测血压？\n" +
                "\n" +
                "1. 怀疑血压异常的人 \n" +
                "\n" +
                "一次测量并不一定能反映真实情况。医学上常见「白大褂高血压」现象，即患者在医生面前紧张焦虑，测的血压较高，而充分适应环境后又很正常。紧张可以使血压比平时高30 mmHg。 \n" +
                "\n" +
                "2. 已经有高血压的人 \n" +
                "\n" +
                "已经有高血压的人，大多需要长期服药控制血压，长期在家测血压对指导治疗有非常大的意义。 \n" +
                "\n" +
                "3. 血压偏高但尚未达到高血压标准的人 \n" +
                "\n" +
                "对于检查血压偏高但尚未达到高血压标准的，按规范应使用专门的动态血压监测仪器，进行24小时监测才能确诊。但普通人也可以通过在家反复测血压的方式粗略了解情况。 \n" +
                "\n" +
                "在家监测血压需要怎样的血压计？\n" +
                "\n" +
                "虽然心内科医生都使用水银血压计来测量血压，但水银血压计单人难以操作，且需要长期训练才能准确测量。所以，在家自测血压首先要购买一个质量可靠的电子血压计。 \n" +
                "\n" +
                "1. 何种规格？ \n" +
                "\n" +
                "中国尚未有血压计的临床准确性检测标准，所以，预算充足的话，应选择标注了通过欧美认证的ESH、AAMI或BHS字样的血压计，例如欧姆龙的多数型号。 \n" +
                "\n" +
                "血压计的袖带规格不同，应根据自己的手臂/手腕粗细进行选择，购买时请详询。 \n" +
                "\n" +
                "此外，电子血压计需要每年校准一次，厂商必须要提供校准服务，否则不值得购买。 \n" +
                "\n" +
                "2. 腕式和上臂式，哪种血压计更好？ \n" +
                "\n" +
                "多数人使用腕式血压计时，因不能将血压计与心脏齐平，增大了误差。而且，研究表明，即便规范使用，上臂式血压计的可靠性也好于腕式。 \n" +
                "\n" +
                "所以，应首选上臂式电子血压计。 \n" +
                "\n" +
                "当然，腕式血压计更小巧便携，而且衣服穿得多时不必脱去袖子也能测量，所以对于经常出行或者寒冷地区的人也是一个选择。 \n" +
                "\n" +
                "市面上还有一种手指血压计，有些还提供手机App来帮助监测和记录。但是，手指的血管很细，张力变化的影响因素太多，测量的结果与上臂血压计偏差很大，而且在多次测量间比较也不稳定。 \n" +
                "\n" +
                "作为专科医生明确表示：手指血压计完全不可靠，没有医学价值。 ");

        Article article3 = new Article();
        article3.setImgRes(R.drawable.article_cancer);
        article3.setTitle("癌症可以依靠精神力量来预防甚至治好吗？");
        article3.setType("疾病");
        article3.setAuthor("丁超");
        article3.setDate("2016年10月4日");
        article3.setContent("某位微博认证为「抗癌协会理事」的主任医师这样认为：\n" +
                "\n" +
                "有爱就不易患癌。美国霍金斯博士说：「很多人生病是因为没有爱，只有痛苦和沮丧，振动指数低于 200。」振动指数也就是人们常说的磁场。最高 1 000，最低为 1。喜欢抱怨、指责、仇恨别人，指数只有三四十，不断指责别人过程当中就消减自己很大的能量。\n" +
                "\n" +
                "这位医生的逻辑是这样的：有爱，就不会有痛苦与沮丧，就不易患癌。\n" +
                "\n" +
                "有「爱」能不能防癌、抗癌呢？\n" +
                "\n" +
                "他所要传播的观点，代表人类自古以来企图用精神力量来控制疾病的美妙空想，非常符合小清新们的胃口。\n" +
                "\n" +
                "只可惜，再美妙，仍旧只是空想。\n" +
                "\n" +
                "直到今天，仍有一些医生会会告诉患者，癌症是由他神经质、内向的个性引起的，要他保持积极乐观态度，想象药物正在杀死癌细胞、免疫细胞正在吞噬癌细胞，跟巫术一样神奇。\n" +
                "\n" +
                "癌症患者的病床边也经常会出现一些宣传宗教的小册子，仿佛信了教，就能陶冶情操，宗教的「大爱」也能帮忙杀灭癌细胞似的——医院边上的小教堂里，人满为患。\n" +
                "\n" +
                "实际上，这并不符合事实。\n" +
                "\n" +
                "科学家曾争论多年\n" +
                "\n" +
                "从 1962 年第一项相关的现代研究开始，大部分研究都提示，人的性格与癌症发生没有关系；有另一部分研究提示，个性与癌症风险有关系。\n" +
                "\n" +
                "但所有这些研究的质量都差了点：\n" +
                "\n" +
                "要么是样本量很小（113 到 1 898 人）；\n" +
                "\n" +
                "要么是没做好实验组与对照组的「均质化」，无法排除偏倚。\n" +
                "\n" +
                "科学家们吵来吵去，谁也不能说服谁；但「个性不好，会导致癌症」的传言，像魔咒般一直笼罩在每个人头上。\n" +
                "\n" +
                "「你不好好听话做个乖孩子，将来就会生癌的！」这样的恐吓，比起「马上就会被狼叼走」可要震撼得多！\n" +
                "\n" +
                "大型研究横空出世\n" +
                "\n" +
                "雄心勃勃的科学家不能满足亦于现状，对任何人类未知的好奇心是科学前进的永恒动力。\n" +
                "\n" +
                "于是，丹麦的科学家设计了一项雄心勃勃的大研究——一项囊括将近 6 万人、随访长达 30 年的大型人群队列研究。\n" +
                "\n" +
                "研究的设计之严谨、对照组之间均质化的程度，都达到这个领域的新高度。\n" +
                "\n" +
                "丹麦科学家不鸣则已、一鸣惊人，是想给争论划上一个可以终结的句号。\n" +
                "\n" +
                "该研究采用艾森克人格问卷，用两个独立的维度来衡量人的个性：外向型-内向型，以及神经质-沉稳型。科学家们发现，无论人的个性是外向还是内向，是神经质还是沉稳，都与癌症的发生风险无关，跟患癌后的死亡率也无关。\n" +
                "\n" +
                "「癌症性格」是对癌患的伤害\n" +
                "\n" +
                "因此，我们必须明确告诉每一位癌症患者：目前没有证据证明，你得了癌症与你的个性有关系，你治疗癌症的预后，也与你的个性无关。\n" +
                "\n" +
                "总有人自作聪明地把性格与癌症联系起来，发明出子虚乌有的「癌症性格」，实际上，会给癌症患者带来罪恶感，误认为是自己性格差、没有爱才得癌。\n" +
                "\n" +
                "癌症患者也就更没必要为了「改变个性」，拿着那些花花绿绿的小册子，被一些宗教粉唬去参加什么宗教活动。");




        articles.add(article);
        articles.add(article2);
        articles.add(article3);

    }
    //将图片装载到数组中
    /*private void initImg() {
        mImageViews = new ImageView[imgIdArray.length];

        for (int i = 0; i < mImageViews.length; i++) {
            ImageView imageView = new ImageView(getActivity());
            mImageViews[i] = imageView;
            imageView.setBackgroundResource(imgIdArray[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        }
    }*/

    /*private void setAdapter() {
        viewPager.setAdapter(new HealthImgAdapter());

        viewPager.setOnPageChangeListener(this);

        viewPager.setCurrentItem(mImageViews.length);
    }*/

    /*public class HealthImgAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            ((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            try {
                ((ViewPager)container).addView(mImageViews[position % mImageViews.length], 0);
            } catch (Exception e) {

            }
            return mImageViews[position % mImageViews.length];
        }
    }*/


    private void initRecycler() {
        mRecycler.addItemDecoration(new RecyclerInsetsDecoration(getActivity()));

        mAdapter = new HealthAdapter(articles);
        mAdapter.setRecyclerListListener(this);
        mRecycler.setAdapter(mAdapter);

        mRefreshSrl.setColorSchemeResources(R.color.red, R.color.green);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
    /*@Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }*/

    /*@Override
    public void onPageSelected(int position) {
        setImageBackground(position % mImageViews.length);
    }*/

    /*private void setImageBackground(int selectItems){
        for(int i=0; i<tips.length; i++){
            if(i == selectItems){
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            }else{
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
        }
    }*/

    @Override
    public void onClick(View v, int position) {
        Intent intent = new Intent(getActivity(), ArticleActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("article", articles.get(position));
        intent.putExtras(bundle);
        startActivity(intent);


    }
}
