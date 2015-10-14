package com.beyole.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class CustomWrapContainer extends ViewGroup {

	public CustomWrapContainer(Context context) {
		super(context);
	}

	public CustomWrapContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomWrapContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 使用系统提供的MarginLayoutParams参数
	 */
	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new MarginLayoutParams(getContext(), attrs);
	}

	/**
	 * 测量view以及子view的宽度和高度，以及位置信息
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 获取上级容器推荐的宽度和高度
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
		// 计算出所有childview的宽度和高度
		measureChildren(widthMeasureSpec, heightMeasureSpec);
		// 记录设置为wrap_content时的宽度和高度
		int height = 0;
		int width = 0;
		// 获得viewgroup的子view的个数
		int mCount = getChildCount();
		int cWidth = 0;
		int cHeight = 0;
		MarginLayoutParams cParams = null;

		// 最终高度取两者之间的最大值
		// 用来计算左边两个view的高度
		int lHeight = 0;
		// 用来计算右边两个view的高度
		int rHeight = 0;

		// 最终宽度取两者之间的最大值
		// 用来计算上面两个view的宽度
		int tWidth = 0;
		// 用来计算下面两个view的宽度
		int bWidth = 0;
		for (int i = 0; i < mCount; i++) {
			// 获取子view
			View childView = getChildAt(i);
			// 获取子view的宽度和高度
			cWidth = childView.getMeasuredWidth();
			cHeight = childView.getMeasuredHeight();
			// 获取子view的布局参数
			cParams = (MarginLayoutParams) childView.getLayoutParams();
			// 上面两个view
			if (i == 0 || i == 1) {
				tWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
			}
			// 下面两个view
			if (i == 2 || i == 3) {
				bWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
			}
			// 左边两个view
			if (i == 0 || i == 2) {
				lHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
			}
			// 右边两个view
			if (i == 1 || i == 3) {
				rHeight += cHeight + cParams.topMargin + cParams.bottomMargin;
			}
			width = Math.max(bWidth, tWidth);
			height = Math.max(lHeight, rHeight);

			// 如果是wrap_content 就设置成计算的宽度和高度，如果不是，则用父容器测量的值
			setMeasuredDimension((widthMode == MeasureSpec.EXACTLY ? sizeWidth : width), (heightMode == MeasureSpec.EXACTLY ? sizeHeight : height));
		}
	}

	/**
	 * onLayout对其所有的子view进行定位
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// 获取子view的个数
		int count = getChildCount();
		int cWidth = 0;
		int cHeight = 0;
		MarginLayoutParams cParams = null;
		// 遍历所有的childview的宽度和高度，以及margin，对其进行布局
		for (int i = 0; i < count; i++) {
			View childView = getChildAt(i);
			cWidth = childView.getMeasuredWidth();
			cHeight = childView.getMeasuredHeight();
			cParams = (MarginLayoutParams) childView.getLayoutParams();
			int cl = 0, ct = 0, cr = 0, cb = 0;
			switch (i) {
			case 0:
				cl = cParams.leftMargin;
				ct = cParams.topMargin;
				break;
			case 1:
				cl = getWidth() - cParams.rightMargin - cWidth - cParams.leftMargin;
				ct = cParams.topMargin;
				break;
			case 2:
				cl = cParams.leftMargin;
				ct = getHeight() - cParams.bottomMargin - cHeight;
				break;
			case 3:
				cl = getWidth() - cParams.rightMargin - cWidth - cParams.leftMargin;
				ct = getHeight() - cParams.bottomMargin - cHeight;
				break;
			}
			cr = cl + cWidth;
			cb = ct + cHeight;
			childView.layout(cl, ct, cr, cb);
		}
	}

}
