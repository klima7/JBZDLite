package com.example.jbzdlite;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.io.IOException;

import static com.example.jbzdlite.Util.println;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ArticleHolder> {

    private Handler handler;
    private ArticleContainer container;

    public RecyclerAdapter(Category category, Context context) {
        container = new ArticleContainer(category);
        handler = new Handler(context.getMainLooper());
    }

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.article_layout, parent, false);
        return new ArticleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleHolder holder, int position) {
        new BindViewTask(holder, position).execute();
    }

    @Override
    public void onViewRecycled(@NonNull ArticleHolder holder) {
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return 1000;
    }


    public class ArticleHolder extends RecyclerView.ViewHolder {
        private Article article;

        private final TextView title;
        private final TextView pluses;
        private final TextView comments;
        private final TextView description;

        private final ImageView image;
        private final TextureView video;
        private final ImageView icon;

        public ArticleHolder(CardView view) {
            super(view);

            title = view.findViewById(R.id.article_title);
            pluses = view.findViewById(R.id.article_pluses);
            comments = view.findViewById(R.id.article_comments);
            description = view.findViewById(R.id.article_description);

            image = view.findViewById(R.id.article_image);
            video = view.findViewById(R.id.article_video);
            icon = view.findViewById(R.id.article_icon);
        }

        public void reset() {
            image.setVisibility(View.VISIBLE);
            title.setText(R.string.article_loading_title);
            pluses.setText(R.string.article_loading_pluses);
            comments.setText(R.string.article_loading_comments);
            Picasso.get().load(R.drawable.loading_image).into(image);
        }

        public void setUnsupported() {
            image.setVisibility(View.VISIBLE);
            Picasso.get().load(R.drawable.unsupported_image).into(image);
        }

        public void setArticle(Article article) {
            this.article = article;

            title.setText(article.getTitle());
            pluses.setText(article.getPluses()+"");
            comments.setText(article.getComments()+"");

            description.setVisibility(View.GONE);
            image.setVisibility(View.GONE);
            video.setVisibility(View.GONE);
            icon.setVisibility(View.GONE);

            if(article.getDescription() != null) {
                description.setVisibility(View.VISIBLE);
                description.setText(article.getDescription());
            }

            if(article instanceof ImageArticle)
                setImageArticle((ImageArticle)article);
            else if(article instanceof  VideoArticle)
                setVideoArticle((VideoArticle)article);
            else
                setUnsupported();
        }

        private void setImageArticle(ImageArticle article) {
            if(article.getImageUrl() != null) {
                image.setVisibility(View.VISIBLE);
                Picasso.get().load(article.getImageUrl()).placeholder(R.drawable.loading_image).into(image);
            }
        }

        private void setVideoArticle(final VideoArticle article) {
            VideoManager manager = new VideoManager(image, video, icon, article);
            manager.prepareToStart();
        }
    }


    private class BindViewTask extends AsyncTask {
        private ArticleHolder holder;
        private int position;

        private Article article;

        public BindViewTask(ArticleHolder holder, int position) {
            this.holder = holder;
            this.position = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            holder.reset();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            boolean success = false;
            while(!success) {
                try {
                    article = container.getArticle(position);
                    success = true;
                } catch (IOException e) { }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            holder.setArticle(article);
        }
    }
}
