package com.limayeneha.articlesearch.view;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.limayeneha.articlesearch.R;
import com.limayeneha.articlesearch.databinding.ListArticleItemBinding;
import com.limayeneha.articlesearch.model.Article;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.limayeneha.articlesearch.helper.CommonHelper.getImageUrl;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> articles;
    private Context context;

    public ArticleAdapter(final Context context, final List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder {

        ListArticleItemBinding binding;


        public ArticleViewHolder(ListArticleItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void update(Article article) {
            binding.setArticle(article);
            binding.executePendingBindings();
        }
    }

    public void setArticles(List<Article> articles) {
        int positionStart = articles.size();
        this.articles.addAll(articles);
        notifyItemRangeInserted(positionStart, articles.size());

    }

    public void clearSearch() {
        this.articles.clear();
        notifyDataSetChanged();
    }

    @android.support.annotation.NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                int viewType) {
        ListArticleItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(
                parent.getContext()), R.layout.list_article_item, parent, false);
        return new ArticleViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull final ArticleViewHolder holder, final int position) {
        final Article article = articles.get(position);
        holder.update(article);
        if(article.multimedia.size() > 0) {
            String thumbnailUrl = getImageUrl(article.multimedia.get(0).url);
            Picasso.get()
                    .load(thumbnailUrl)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.binding.articleThumbnail, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.binding.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            Picasso.get()
                                    .load(thumbnailUrl)
                                    .error(R.drawable.ic_launcher_background)
                                    .into(holder.binding.articleThumbnail, new Callback() {
                                        @Override
                                        public void onSuccess() {
                                            holder.binding.progressBar.setVisibility(View.GONE);
                                        }

                                        @Override
                                        public void onError(Exception e) {
                                            Log.v("Picasso", "Could not fetch image");
                                        }
                                    });

                        }
                    });
        } else {
            holder.binding.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }
}
