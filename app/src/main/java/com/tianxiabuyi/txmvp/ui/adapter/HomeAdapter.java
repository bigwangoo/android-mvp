package com.tianxiabuyi.txmvp.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianxiabuyi.txmvp.R;
import com.tianxiabuyi.txmvp.mvp.model.bean.UserBean;

import java.util.List;

/**
 * Created in 2017/9/22 13:48.
 *
 * @author Wang YaoDong.
 */
public class HomeAdapter extends BaseQuickAdapter<UserBean, BaseViewHolder> {

    public HomeAdapter(@Nullable List<UserBean> data) {
        super(R.layout.item_home, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UserBean item) {
        Glide.with(mContext)
                .load(item.getAvatarUrl())
                .into((ImageView) helper.getView(R.id.iv_avatar));
        helper.setText(R.id.tv_name, item.getId() + "");
    }
}
