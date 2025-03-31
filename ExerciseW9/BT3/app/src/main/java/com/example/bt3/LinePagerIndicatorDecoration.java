package com.example.bt3;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LinePagerIndicatorDecoration extends RecyclerView.ItemDecoration {

    private final Paint mPaintInactive = new Paint();
    private final Paint mPaintActive = new Paint();

    // Define indicator properties (colors, size, spacing)
    private final int mIndicatorHeight = (int) (Resources.getSystem().getDisplayMetrics().density * 4); // Example height
    private final float mIndicatorItemLength = Resources.getSystem().getDisplayMetrics().density * 16; // Example length
    private final float mIndicatorItemPadding = Resources.getSystem().getDisplayMetrics().density * 8; // Example padding
    private final float mIndicatorStrokeWidth = Resources.getSystem().getDisplayMetrics().density * 2; // Example stroke width

    public LinePagerIndicatorDecoration() {
        // Setup paint colors, styles etc.
        mPaintInactive.setColor(0x66FFFFFF); // Example inactive color (semi-transparent white)
        mPaintActive.setColor(0xFFFFFFFF);   // Example active color (white)

        // Example paint setup (adjust as needed)
        mPaintInactive.setStyle(Paint.Style.STROKE);
        mPaintInactive.setStrokeWidth(mIndicatorStrokeWidth);
        mPaintInactive.setAntiAlias(true);

        mPaintActive.setStyle(Paint.Style.FILL_AND_STROKE); // Or just FILL
        mPaintActive.setStrokeWidth(mIndicatorStrokeWidth);
        mPaintActive.setAntiAlias(true);

        // The DP calculation from Slide 7 likely belongs here or is used to init these values
        // private final float DP = Resources.getSystem().getDisplayMetrics().density;
    }


    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        RecyclerView.Adapter adapter = parent.getAdapter();
        if (adapter == null || adapter.getItemCount() <= 0) {
            return;
        }
        int itemCount = adapter.getItemCount();

        // Calculate total width needed for indicators
        float totalLength = mIndicatorItemLength * itemCount;
        float paddingBetweenItems = Math.max(0, itemCount - 1) * mIndicatorItemPadding;
        float indicatorTotalWidth = totalLength + paddingBetweenItems;
        float indicatorStartX = (parent.getWidth() - indicatorTotalWidth) / 2F;
        float indicatorPosY = parent.getHeight() - mIndicatorHeight / 2F - 10; // Adjust Y position (e.g. -10 from bottom)

        // Determine the active page/position
        int activePosition = 0;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            activePosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
        } else if (layoutManager instanceof GridLayoutManager) {
            // Adjust logic for GridLayoutManager if needed, e.g., based on span count
            activePosition = ((GridLayoutManager) layoutManager).findFirstVisibleItemPosition();
        }

        if (activePosition == RecyclerView.NO_POSITION) {
            return;
        }

        // Draw indicators
        drawIndicators(c, indicatorStartX, indicatorPosY, itemCount, activePosition);
    }

    private void drawIndicators(Canvas c, float startX, float startY, int itemCount, int activePosition) {
        float currentX = startX;
        for (int i = 0; i < itemCount; i++) {
            // Draw a line segment
            c.drawLine(currentX, startY, currentX + mIndicatorItemLength, startY,
                    (i == activePosition) ? mPaintActive : mPaintInactive);
            // Move to the start of the next indicator's position
            currentX += (mIndicatorItemLength + mIndicatorItemPadding);
        }
    }

    // Override getItemOffsets if you need to add space for the indicator within the layout bounds
    // @Override
    // public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
    //     super.getItemOffsets(outRect, view, parent, state);
    //     // e.g., outRect.bottom = mIndicatorHeight + padding;
    // }
}
