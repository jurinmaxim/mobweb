package hu.bme.jurinmaxim.mobwebnhf.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.jurinmaxim.mobwebnhf.ExerciseActivity;
import hu.bme.jurinmaxim.mobwebnhf.R;
import hu.bme.jurinmaxim.mobwebnhf.data.Day;
import hu.bme.jurinmaxim.mobwebnhf.data.WorkoutPlan;
import hu.bme.jurinmaxim.mobwebnhf.util.LetterImageView;


public class DayAdapter extends RecyclerView.Adapter<DayAdapter.DayViewHolder> {

    public final ArrayList<Day> days;
    private DayClickListener listener;
    private Context context;

    public DayAdapter(Context _context, DayClickListener listener) {
        this.listener = listener;
        days = new ArrayList<>();
        context = _context;

    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_day, parent, false);
        return new DayViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        Day day = days.get(position);
        holder.categoryTextView.setText(day.dayOfWeek.name());
        holder.letterImageView.setLetter(day.dayOfWeek.name().charAt(0));
        holder.letterImageView.setOval(true);

        holder.day = day;
    }

    public void addDay(Day day) {
        days.add(day);
        notifyItemInserted(days.size() - 1);
    }

    public void deleteDay(Day day) {
        int temp = days.indexOf(day);
        days.remove(day);
        notifyItemRemoved(temp);
    }

    public void update(List<Day> dayList) {
        days.clear();
        days.addAll(dayList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    public interface DayClickListener {

        void onDayDeleted(Day day);
    }


    class DayViewHolder extends RecyclerView.ViewHolder {

        LetterImageView letterImageView;
        TextView categoryTextView;
        ImageButton removeButton;

        Day day;

        DayViewHolder(View itemView) {
            super(itemView);
            letterImageView = itemView.findViewById(R.id.DayLetterImageView);
            categoryTextView = itemView.findViewById(R.id.DayCategoryTextView);
            removeButton = itemView.findViewById(R.id.DayRemoveButton);


            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, ExerciseActivity.class);
                int dayID = -1;
                WorkoutPlan plan = WorkoutPlan.findById(WorkoutPlan.class, day.planID);
                plan.days = WorkoutPlan.fromString(plan.dayString);
                for (int i = 0; i < days.size(); i++)
                    if (days.get(i).dayId == day.dayId)
                        dayID = i;
                intent.putExtra("name", day.dayOfWeek.toString());
                intent.putExtra("planId", day.planID);
                intent.putExtra("dayId", dayID);
                intent.putExtra("parity", day.parity);
                context.startActivity(intent);
            });
            removeButton.setOnClickListener(v -> listener.onDayDeleted(day));
        }
    }
}

