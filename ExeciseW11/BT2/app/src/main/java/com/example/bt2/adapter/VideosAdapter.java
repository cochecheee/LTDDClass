package com.example.bt2.adapter;// File: VideosAdapter.java
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;
import android.widget.Toast; // Để debug hoặc thông báo lỗi
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bt2.R;
import com.example.bt2.model.Video1Model;

import java.util.List;
// import com.yourpackage.R; // Thay bằng package R của bạn

// Sử dụng RecyclerView.Adapter tiêu chuẩn
public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.MyViewHolder> {

    private Context context;
    private List<Video1Model> videoList;

    public VideosAdapter(Context context, List<Video1Model> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho từng item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_video_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Lấy data từ list tại vị trí tương ứng
        Video1Model currentVideo = videoList.get(position);
        // Giao việc hiển thị cho ViewHolder
        holder.bindData(currentVideo);
    }

    @Override
    public int getItemCount() {
        // Trả về số lượng item trong list
        return videoList != null ? videoList.size() : 0;
    }

    // --- ViewHolder Class ---
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // Khai báo các View trong item layout
        VideoView videoView;
        ProgressBar videoProgressBar;
        TextView textVideoTitle, textVideoDescription;
        ImageView imPerson, favorites, imShare, imMore;

        // Biến tạm để lưu trạng thái like của item này (cần giải pháp lưu trữ tốt hơn)
        private boolean isCurrentItemFavorite = false;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ View từ layout
            videoView = itemView.findViewById(R.id.videoView);
            videoProgressBar = itemView.findViewById(R.id.videoProgressBar);
            textVideoTitle = itemView.findViewById(R.id.textVideoTitle);
            textVideoDescription = itemView.findViewById(R.id.textVideoDescription);
            imPerson = itemView.findViewById(R.id.imPerson);
            favorites = itemView.findViewById(R.id.favorites);
            imShare = itemView.findViewById(R.id.imShare);
            imMore = itemView.findViewById(R.id.imMore);
        }
// (Hàm bindData ở slide tiếp theo)
void bindData(final Video1Model model) {
    // Gán text
    textVideoTitle.setText(model.getTitle());
    textVideoDescription.setText(model.getDesc());

    // Reset trạng thái ProgressBar và Favorite button
    videoProgressBar.setVisibility(View.VISIBLE);
    isCurrentItemFavorite = false; // TODO: Lấy trạng thái like thực tế
    favorites.setImageResource(R.drawable.ic_favorite); // Icon mặc định

    // Gán Video URI
    String videoUrl = model.getUrl();
    if (videoUrl != null && !videoUrl.isEmpty()) {
        Uri videoUri = Uri.parse(videoUrl);
        videoView.setVideoURI(videoUri);
    } else {
        // Xử lý trường hợp URL null hoặc rỗng (ví dụ: hiển thị ảnh placeholder)
        videoProgressBar.setVisibility(View.GONE);
        Toast.makeText(itemView.getContext(), "Video URL is missing!", Toast.LENGTH_SHORT).show();
    }

    // Listener khi video sẵn sàng play
    videoView.setOnPreparedListener(mediaPlayer -> {
        videoProgressBar.setVisibility(View.GONE); // Ẩn loading
        mediaPlayer.start(); // Bắt đầu phát
        mediaPlayer.setLooping(true); // Tự động lặp lại video

        // --- Điều chỉnh tỷ lệ khung hình (Aspect Ratio) ---
        try {
            float videoRatio = mediaPlayer.getVideoWidth() / (float) mediaPlayer.getVideoHeight();
            float screenRatio = videoView.getWidth() / (float) videoView.getHeight();
            float scale = videoRatio / screenRatio;
            if (scale >= 1f) {
                videoView.setScaleX(scale);
                videoView.setScaleY(1f); // Reset scale Y
            } else {
                videoView.setScaleX(1f); // Reset scale X
                videoView.setScaleY(1f / scale);
            }
        } catch (Exception e) {
            Log.e("VideoRatioError", "Error calculating aspect ratio", e);
        }
        // ----------------------------------------------------
    });

    // Listener khi video hoàn thành (đã setLooping nên ít khi cần)
    // videoView.setOnCompletionListener(mediaPlayer -> mediaPlayer.start());

    // Listener khi có lỗi video
    videoView.setOnErrorListener((mediaPlayer, what, extra) -> {
        videoProgressBar.setVisibility(View.GONE);
        Toast.makeText(itemView.getContext(), "Cannot play video", Toast.LENGTH_SHORT).show();
        Log.e("VideoError", "Error playing video: what=" + what + ", extra=" + extra);
        return true; // Đã xử lý lỗi
    });

    // Xử lý sự kiện nhấn nút Favorite
    favorites.setOnClickListener(v -> {
        isCurrentItemFavorite = !isCurrentItemFavorite; // Đảo trạng thái
        if (isCurrentItemFavorite) {
            favorites.setImageResource(R.drawable.ic_fill_favorite); // Icon đã like
            // TODO: Gửi request lên Supabase (ví dụ: gọi Edge Function) để lưu like
            Log.d("FavoriteClick", "Video ID " + model.getId() + " Liked");
        } else {
            favorites.setImageResource(R.drawable.ic_favorite); // Icon chưa like
            // TODO: Gửi request lên Supabase để bỏ like
            Log.d("FavoriteClick", "Video ID " + model.getId() + " Unliked");
        }
    });

    // TODO: Thêm OnClickListener cho các nút imShare, imMore, imPerson nếu cần
    imShare.setOnClickListener(v -> Toast.makeText(itemView.getContext(), "Share clicked", Toast.LENGTH_SHORT).show());
    imMore.setOnClickListener(v -> Toast.makeText(itemView.getContext(), "More clicked", Toast.LENGTH_SHORT).show());
    imPerson.setOnClickListener(v -> Toast.makeText(itemView.getContext(), "Person clicked", Toast.LENGTH_SHORT).show());
}
    } // Kết thúc MyViewHolder
} // Kết thúc VideosAdapter