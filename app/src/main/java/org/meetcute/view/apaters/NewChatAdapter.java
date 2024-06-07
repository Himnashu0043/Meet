package org.meetcute.view.apaters;


import static org.meetcute.view.apaters.TimeUtils.dateHeader;
import static org.meetcute.view.apaters.TimeUtils.getDate;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.meetcute.R;
import org.meetcute.databinding.ChatSceenItemBinding;
import org.meetcute.databinding.IstypingLoaderBinding;
import org.meetcute.databinding.ItemChatReceivedBinding;
import org.meetcute.network.data.model.ChatMessageListRes;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class NewChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "NewChatAdapter";
    private final Context context;
    private final String userId;
    private final ArrayList<ChatMessageListRes.ChatData> list = new ArrayList<>();

    public void addAt(ChatMessageListRes.ChatData item, int position) {
        list.add(position, item);
        notifyItemInserted(position);
    }

    public int addItem(ChatMessageListRes.ChatData item) {
        removeTyping();
        int itemCount = list.size();
        list.add(item);
        notifyItemInserted(itemCount);
        return itemCount;
    }

    public NewChatAdapter(Context context, String userId) {
        this.context = context;
        this.userId = userId;
    }


    public void addTyping() {
        try {
            int count = (int) list.stream().count();
            list.add(new ChatMessageListRes.TypingData());
            notifyItemInserted(count);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void removeTyping() {
        int lastposition = getItemCount() - 1;
        if (!list.isEmpty() && list.get(lastposition) instanceof ChatMessageListRes.TypingData) {
            try {
                list.remove(lastposition);
                notifyItemRemoved(lastposition);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void addLoader() {
        try {
            list.add(0, null);
            notifyItemInserted(0);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void removeLoader() {
        if (!list.isEmpty() && list.get(0) == null) {
            try {
                list.remove(0);
                notifyItemRemoved(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class TypingViewHolder extends RecyclerView.ViewHolder {

        private final IstypingLoaderBinding binding;

        public TypingViewHolder(@NonNull IstypingLoaderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ChatMessageListRes.ChatData item) {
            Glide.with(binding.ivProfile).load(item.getViewer().getProfileImage()).into(binding.ivProfile);
        }
    }

    private class TextSendViewHolder extends RecyclerView.ViewHolder {

        private final ChatSceenItemBinding binding;

        public TextSendViewHolder(@NonNull ChatSceenItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ChatMessageListRes.ChatData item) {
            binding.tvChat.setVisibility(View.VISIBLE);
            binding.tvimg.setVisibility(View.GONE);
            binding.tvChat.setText(item.getMessage().getMessage());
            binding.tvMint.setText(TimeUtils.parseTimeFormat(item.getCreatedAt()));
            Pair<Boolean, String> pair = shouldShowDate(getAbsoluteAdapterPosition());
            if (pair.first) {
                binding.llToday.setVisibility(View.VISIBLE);
                binding.tvToday.setText(pair.second);
            } else {
                binding.llToday.setVisibility(View.GONE);
            }
            Glide.with(binding.ivProfile).load(item.getBroadcaster().getProfileImage()).into(binding.ivProfile);
        }
    }

    private class MediaSendViewHolder extends RecyclerView.ViewHolder {
        private final ChatSceenItemBinding binding;

        public MediaSendViewHolder(@NonNull ChatSceenItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ChatMessageListRes.ChatData item) {
            binding.tvChat.setVisibility(View.INVISIBLE);
            binding.tvimg.setVisibility(View.VISIBLE);

            binding.tvMint.setText(TimeUtils.parseTimeFormat(item.getCreatedAt()));
            Pair<Boolean, String> pair = shouldShowDate(getAbsoluteAdapterPosition());
            if (pair.first) {
                binding.llToday.setVisibility(View.VISIBLE);
                binding.tvToday.setText(pair.second);
            } else {
                binding.llToday.setVisibility(View.GONE);
            }
            //showImg = item.getMessage().getMessage();
            Glide.with(binding.tvimg).load(item.getMessage().getMessage()).into(binding.tvimg);
            Glide.with(binding.ivProfile).load(item.getBroadcaster().getProfileImage()).into(binding.ivProfile);
            binding.tvimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogProile(item.getMessage().getMessage());
                }
            });
        }
    }
    private class TextReceiveViewHolder extends RecyclerView.ViewHolder {
        private final ItemChatReceivedBinding binding;

        public TextReceiveViewHolder(@NonNull ItemChatReceivedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ChatMessageListRes.ChatData item) {
            binding.tvChat.setVisibility(View.VISIBLE);
            binding.tvimg.setVisibility(View.GONE);
            binding.tvChat.setText(item.getMessage().getMessage());
            binding.tvMint.setText(TimeUtils.parseTimeFormat(item.getCreatedAt()));
            Pair<Boolean, String> pair = shouldShowDate(getAbsoluteAdapterPosition());
            if (pair.first) {
                binding.llToday.setVisibility(View.VISIBLE);
                binding.tvToday.setText(pair.second);
            } else {
                binding.llToday.setVisibility(View.GONE);
            }
            Glide.with(binding.ivProfile).load(item.getViewer().getProfileImage()).into(binding.ivProfile);

        }
    }

    private class MediaReceiveViewHolder extends RecyclerView.ViewHolder {
        ItemChatReceivedBinding binding;

        public MediaReceiveViewHolder(@NonNull ItemChatReceivedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ChatMessageListRes.ChatData item) {
            binding.tvChat.setVisibility(View.INVISIBLE);
            binding.tvimg.setVisibility(View.VISIBLE);
            binding.tvMint.setText(TimeUtils.parseTimeFormat(item.getCreatedAt()));
            Pair<Boolean, String> pair = shouldShowDate(getAbsoluteAdapterPosition());
            if (pair.first) {
                binding.llToday.setVisibility(View.VISIBLE);
                binding.tvToday.setText(pair.second);
            } else {
                binding.llToday.setVisibility(View.GONE);
            }
            Glide.with(binding.tvimg).load(item.getMessage().getMessage()).into(binding.tvimg);
            Glide.with(binding.ivProfile).load(item.getBroadcaster().getProfileImage()).into(binding.ivProfile);
            binding.tvimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogProile(item.getMessage().getMessage());
                }
            });
        }
    }

    /*private class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(@NonNull IstypingLoaderBinding binding) {
            super(binding.getRoot());
        }
    }*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        switch (viewType) {
            case USER_TYPING:
                return new TypingViewHolder(IstypingLoaderBinding.inflate(inflater, viewGroup, false));

         /*   case LOADING:
                return new LoadingViewHolder(IstypingLoaderBinding.inflate(inflater, viewGroup, false));*/
            case TEXT_SEND:
                return new TextSendViewHolder(ChatSceenItemBinding.inflate(inflater, viewGroup, false));

            case TEXT_RECEIVED:
                return new TextReceiveViewHolder(ItemChatReceivedBinding.inflate(inflater, viewGroup, false));

            case MEDIA_SENT:
                return new MediaSendViewHolder(ChatSceenItemBinding.inflate(inflater, viewGroup, false));

            case MEDIA_RECEIVED:
                return new MediaReceiveViewHolder(ItemChatReceivedBinding.inflate(inflater, viewGroup, false));
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position) == null) {
            return LOADING;
        } else if (list.get(position) instanceof ChatMessageListRes.TypingData) {
            return USER_TYPING;
        } else if (!Objects.equals(list.get(position).getSentBy(), "Viewer")) {
            if (list.get(position).getMessage().getMtype().equals("media")) {
                return MEDIA_SENT;
            } else {
                return TEXT_SEND;
            }
        } else {
            if (list.get(position).getMessage().getMtype().equals("media")) {
                return MEDIA_RECEIVED;
            } else {
                return TEXT_RECEIVED;
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        try {
            Log.d("Tag", "onCreateViewHolder: ViewTYpe = " + holder);
            switch (holder.getItemViewType()) {
                case TEXT_SEND:
                    ((TextSendViewHolder) holder).bind(list.get(position));
                    break;

                case TEXT_RECEIVED:
                    ((TextReceiveViewHolder) holder).bind(list.get(position));
                    break;

                case MEDIA_SENT:
                    ((MediaSendViewHolder) holder).bind(list.get(position));
                    break;

                case MEDIA_RECEIVED:
                    ((MediaReceiveViewHolder) holder).bind(list.get(position));
                    break;

                case USER_TYPING:
                    ((TypingViewHolder) holder).bind(list.get(position));
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Calendar getCalender(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private Pair<Boolean, String> shouldShowDate(int position) {
        if (position == 0) {
            if (isSameDay(Calendar.getInstance(), getCalender(getDate(list.get(position).getCreatedAt())))) {
                return new Pair<>(true, dateHeader(list.get(position).getCreatedAt())); // Always show the date for the first message
            } else {
                return new Pair<>(true, dateHeader(list.get(position).getCreatedAt()));
            }
        } else {
            Calendar currentCal = getCalender(getDate(list.get(position).getCreatedAt()));
            Calendar previousCal = getCalender(getDate(list.get(position - 1).getCreatedAt()));
            if (!isSameDay(currentCal, previousCal)) {
                return new Pair<>(true, dateHeader(list.get(position).getCreatedAt()));
            } else {
                return new Pair<>(false, "");
            }
        }
    }


    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
    private static final int LOADING = 0;
    private static final int TEXT_SEND = 1;
    private static final int USER_TYPING = 5;
    private static final int TEXT_RECEIVED = 2;
    private static final int MEDIA_SENT = 3;
    private static final int MEDIA_RECEIVED = 4;

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void dialogProile(String showImg) {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_profile_owner);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        ImageView imgGallery = dialog.findViewById(R.id.ivProImg);
        Glide.with(imgGallery).load(showImg).into(imgGallery);
        dialog.show();
    }
}
