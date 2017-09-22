package com.zalologin.ebook;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Dùng lại từ FavoriteAdapter, nên không change lại tên biến.
 * Re-use from FavoriteAdapter. So some field names do not change.
 * <p>
 * Created by Administrator on 28/06/2017.
 */

public class EbookChapterAdapter extends RecyclerView.Adapter {

    private Context ctx;
    private static final int EMPTY_LOADING_SIZE = 0;
    private static final int EMPTY_PLACEHOLDER_SIZE = 1;
    private OnClickEbookChapter onClickChapterModel;
    private String latestViewedChapter = "0";

    public EbookChapterAdapter(Context ctx, OnClickEbookChapter onClickChapterModel, String latestViewedChapter) {
        this.ctx = ctx;
        this.onClickChapterModel = onClickChapterModel;
        this.latestViewedChapter = latestViewedChapter;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
