package com.nju.android.health.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nju.android.health.R;
import com.nju.android.health.model.data.Article;
import com.nju.android.health.utils.RecyclerViewClickListener;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by 57248 on 2016/10/10.
 */

public class HealthAdapter extends RecyclerView.Adapter<HealthViewHolder> {

    private List<Article> articleList;

    private RecyclerViewClickListener listener;

    public HealthAdapter(List<Article> articles) {
        this.articleList = articles;
    }

    public void setRecyclerListListener(RecyclerViewClickListener listListener) {
        this.listener = listListener;
    }

    @Override
    public HealthViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_health, parent, false);
        return new HealthViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(HealthViewHolder holder, int position) {
        holder.img.setImageResource(articleList.get(position).getImgRes());
        holder.title.setText(articleList.get(position).getTitle());
        holder.type.setText(articleList.get(position).getType());
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
}
class HealthViewHolder extends RecyclerView.ViewHolder implements View.OnTouchListener {
    private final RecyclerViewClickListener mClickListener;
    LinearLayout ll;
    ImageView img;
    TextView title;
    TextView type;

    public HealthViewHolder(View itemView, RecyclerViewClickListener listener) {
        super(itemView);

        ll = (LinearLayout) itemView.findViewById(R.id.ll_health_item);
        img = (ImageView) itemView.findViewById(R.id.health_item_img);
        title = (TextView) itemView.findViewById(R.id.health_item_title);
        type = (TextView) itemView.findViewById(R.id.health_item_type);
        ll.setOnTouchListener(this);

        this.mClickListener = listener;
    }
    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_MOVE) {

            mClickListener.onClick(view, getPosition());
        }
        return true;
    }
}
