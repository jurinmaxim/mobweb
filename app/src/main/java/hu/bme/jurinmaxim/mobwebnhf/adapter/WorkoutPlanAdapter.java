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

import hu.bme.jurinmaxim.mobwebnhf.DayActivity;
import hu.bme.jurinmaxim.mobwebnhf.R;
import hu.bme.jurinmaxim.mobwebnhf.data.Day;
import hu.bme.jurinmaxim.mobwebnhf.data.WorkoutPlan;
import hu.bme.jurinmaxim.mobwebnhf.util.LetterImageView;

public class WorkoutPlanAdapter extends RecyclerView.Adapter<WorkoutPlanAdapter.WorkoutViewHolder> {

    private final ArrayList<WorkoutPlan> plans;

    private WorkoutPlanClickListener listener;
    private Context context;

    public WorkoutPlanAdapter(Context _context, WorkoutPlanClickListener listener) {
        this.listener = listener;
        plans = new ArrayList<>();
        context = _context;
    }

    @NonNull
    @Override
    public WorkoutViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_workout_plan, parent, false);
        return new WorkoutViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull WorkoutViewHolder holder, int position) {
        WorkoutPlan plan = plans.get(position);
        holder.nameTextView.setText(plan.name);
        holder.categoryTextView.setText(plan.category.name());
        holder.letterImageView.setLetter(plan.name.charAt(0));
        holder.letterImageView.setOval(true);

        holder.plan = plan;
    }

    public void addPlan(WorkoutPlan plan) {
        plans.add(plan);
        notifyItemInserted(plans.size() - 1);
    }

    public void deletePlan(WorkoutPlan plan) {
        int temp = plans.indexOf(plan);
        plans.remove(plan);
        notifyItemRemoved(temp);
    }

    public void update(List<WorkoutPlan> planList) {
        plans.clear();
        plans.addAll(planList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return plans.size();
    }

    private String shareData(WorkoutPlan plan) {
        String data = "";
        data = data.concat("name: " + plan.name + "\n");
        data = data.concat("type: " + plan.category + "\n\n");
        if (plan.eDayString != null)
            data = data.concat("Odd week: \n\n");
        plan.days = WorkoutPlan.fromString(plan.dayString);
        for (int i = 0; i < plan.days.size(); i++) {
            data = data.concat(plan.days.get(i).dayOfWeek.name() + ":\n\n");
            plan.days.get(i).exercises = Day.fromString(plan.days.get(i).exerciseString);
            for (int j = 0; j < plan.days.get(i).exercises.size(); j++) {
                data = data.concat("exercise: " + plan.days.get(i).exercises.get(j).exercise + "\n");
                data = data.concat("type: " + plan.days.get(i).exercises.get(j).category.name() + "\n");
                data = data.concat("sets: " + plan.days.get(i).exercises.get(j).sets + ", reps: " + plan.days.get(i).exercises.get(j).repetitions + "\n\n");
            }
        }
        if (plan.eDayString != null) {
            plan.eDays = WorkoutPlan.fromString(plan.eDayString);
            data = data.concat("Even week: \n\n");
            for (int i = 0; i < plan.eDays.size(); i++) {
                data = data.concat(plan.eDays.get(i).dayOfWeek.name() + ":\n\n");
                plan.eDays.get(i).exercises = Day.fromString(plan.eDays.get(i).exerciseString);
                for (int j = 0; j < plan.eDays.get(i).exercises.size(); j++) {
                    data = data.concat("exercise: " + plan.eDays.get(i).exercises.get(j).exercise + "\n");
                    data = data.concat("type: " + plan.eDays.get(i).exercises.get(j).category.name() + "\n");
                    data = data.concat("sets: " + plan.eDays.get(i).exercises.get(j).sets + ", reps: " + plan.eDays.get(i).exercises.get(j).repetitions + "\n\n");
                }
            }
        }
        return data;
    }

    public interface WorkoutPlanClickListener {

        void onWorkoutPlanDeleted(WorkoutPlan plan);
    }

    class WorkoutViewHolder extends RecyclerView.ViewHolder {

        LetterImageView letterImageView;
        TextView nameTextView;
        TextView categoryTextView;
        ImageButton shareButton;
        ImageButton removeButton;

        WorkoutPlan plan;

        WorkoutViewHolder(View itemView) {
            super(itemView);
            letterImageView = itemView.findViewById(R.id.WorkoutPlanLetterImageView);
            nameTextView = itemView.findViewById(R.id.WorkoutPlanNameTextView);
            categoryTextView = itemView.findViewById(R.id.WorkoutPlanCategoryTextView);
            shareButton = itemView.findViewById(R.id.WorkoutPlanShareButton);
            removeButton = itemView.findViewById(R.id.WorkoutPlanRemoveButton);


            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DayActivity.class);
                intent.putExtra("name", plan.name);
                intent.putExtra("id", plan.getId());
                context.startActivity(intent);
            });
            removeButton.setOnClickListener(v -> listener.onWorkoutPlanDeleted(plan));
            shareButton.setOnClickListener(v -> {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareData(plan));
                shareIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(shareIntent, context.getResources().getText(R.string.send_to)));
            });
        }
    }
}

