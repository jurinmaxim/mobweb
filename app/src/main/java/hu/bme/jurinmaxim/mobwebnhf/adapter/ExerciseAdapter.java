package hu.bme.jurinmaxim.mobwebnhf.adapter;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hu.bme.jurinmaxim.mobwebnhf.R;
import hu.bme.jurinmaxim.mobwebnhf.data.Exercise;
import hu.bme.jurinmaxim.mobwebnhf.util.LetterImageView;


public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {

    public final ArrayList<Exercise> exercises;
    private ExerciseClickListener listener;

    public ExerciseAdapter(ExerciseClickListener listener) {
        this.listener = listener;
        exercises = new ArrayList<>();
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, int position) {
        Exercise item = exercises.get(position);
        //holder.nameTextView.setText(item.name);
        holder.categoryTextView.setText("Category: " + item.category.name());
        holder.exerciseTextView.setText("Exercise: " + item.exercise);
        holder.setsTextView.setText(Integer.toString(item.sets) + " sets");
        holder.repTextView.setText(Integer.toString(item.repetitions) + " repetitions");
        holder.letterImageView.setLetter(item.category.name().charAt(0));
        holder.letterImageView.setOval(true);

        holder.item = item;
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
        notifyItemInserted(exercises.size() - 1);
    }

    public void deleteExercise(Exercise exercise) {
        int temp = exercises.indexOf(exercise);
        exercises.remove(exercise);
        notifyItemRemoved(temp);
    }

    public void update(List<Exercise> exerciseList) {
        exercises.clear();
        exercises.addAll(exerciseList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return exercises.size();
    }

    public interface ExerciseClickListener {
        void onExerciseDeleted(Exercise exercise);
    }

    class ExerciseViewHolder extends RecyclerView.ViewHolder {

        LetterImageView letterImageView;
        TextView setsTextView;
        TextView repTextView;
        TextView exerciseTextView;
        TextView categoryTextView;
        ImageButton removeButton;

        Exercise item;

        ExerciseViewHolder(View itemView) {
            super(itemView);

            letterImageView = itemView.findViewById(R.id.ExerciseLetterImageView);
            setsTextView = itemView.findViewById(R.id.SetsTextView);
            repTextView = itemView.findViewById(R.id.RepTextView);
            exerciseTextView = itemView.findViewById(R.id.ExerciseTextView);
            categoryTextView = itemView.findViewById(R.id.ExerciseCategoryTextView);
            removeButton = itemView.findViewById(R.id.ExerciseRemoveButton);


            itemView.setOnClickListener(v -> {
            });
            removeButton.setOnClickListener(v -> listener.onExerciseDeleted(item));
        }
    }
}

