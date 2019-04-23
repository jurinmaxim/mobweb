package hu.bme.jurinmaxim.mobwebnhf;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import hu.bme.jurinmaxim.mobwebnhf.adapter.ExerciseAdapter;
import hu.bme.jurinmaxim.mobwebnhf.data.Day;
import hu.bme.jurinmaxim.mobwebnhf.data.Exercise;
import hu.bme.jurinmaxim.mobwebnhf.data.WorkoutPlan;
import hu.bme.jurinmaxim.mobwebnhf.fragment.NewExerciseDialogFragment;

public class ExerciseActivity extends AppCompatActivity
        implements NewExerciseDialogFragment.NewExerciseDialogListener,
        ExerciseAdapter.ExerciseClickListener {

    private ExerciseAdapter adapter;
    private long planId;
    private int dayId;
    private boolean odd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String title = intent.getStringExtra("name");
        planId = intent.getLongExtra("planId", 0);
        dayId = intent.getIntExtra("dayId", 0);
        odd = intent.getBooleanExtra("parity", true);

        setTitle(title);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> new NewExerciseDialogFragment().show(getSupportFragmentManager(), NewExerciseDialogFragment.TAG));

        RecyclerView recyclerView = findViewById(R.id.ExerciseRecyclerView);
        adapter = new ExerciseAdapter(this);
        loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onExerciseDeleted(Exercise exercise) {
        if (odd) {
            adapter.deleteExercise(exercise);
            WorkoutPlan workoutPlan = WorkoutPlan.findById(WorkoutPlan.class, planId);
            workoutPlan.days = WorkoutPlan.fromString(workoutPlan.dayString);
            workoutPlan.days.get(dayId).exercises = adapter.exercises;
            workoutPlan.days.get(dayId).exerciseString = workoutPlan.days.get(dayId).toString();
            workoutPlan.dayString = workoutPlan.toString(workoutPlan.days);
            workoutPlan.save();
        } else {
            adapter.deleteExercise(exercise);
            WorkoutPlan workoutPlan = WorkoutPlan.findById(WorkoutPlan.class, planId);
            workoutPlan.eDays = WorkoutPlan.fromString(workoutPlan.eDayString);
            workoutPlan.eDays.get(dayId).exercises = adapter.exercises;
            workoutPlan.eDays.get(dayId).exerciseString = workoutPlan.eDays.get(dayId).toString();
            workoutPlan.eDayString = workoutPlan.toString(workoutPlan.eDays);
            workoutPlan.save();
        }
    }

    @Override
    public void onExerciseCreated(Exercise exercise) {
        if (odd) {
            adapter.addExercise(exercise);
            WorkoutPlan workoutPlan = WorkoutPlan.findById(WorkoutPlan.class, planId);
            workoutPlan.days = WorkoutPlan.fromString(workoutPlan.dayString);
            workoutPlan.days.get(dayId).exercises = Day.fromString(workoutPlan.days.get(dayId).exerciseString);
            workoutPlan.days.get(dayId).exercises.add(exercise);
            workoutPlan.days.get(dayId).exerciseString = workoutPlan.days.get(dayId).toString();
            workoutPlan.dayString = workoutPlan.toString(workoutPlan.days);
            workoutPlan.save();
        } else {
            adapter.addExercise(exercise);
            WorkoutPlan workoutPlan = WorkoutPlan.findById(WorkoutPlan.class, planId);
            workoutPlan.eDays = WorkoutPlan.fromString(workoutPlan.eDayString);
            workoutPlan.eDays.get(dayId).exercises = Day.fromString(workoutPlan.eDays.get(dayId).exerciseString);
            workoutPlan.eDays.get(dayId).exercises.add(exercise);
            workoutPlan.eDays.get(dayId).exerciseString = workoutPlan.eDays.get(dayId).toString();
            workoutPlan.eDayString = workoutPlan.toString(workoutPlan.eDays);
            workoutPlan.save();
        }
    }

    private void loadItemsInBackground() {
        if (odd) {
            WorkoutPlan plan = WorkoutPlan.findById(WorkoutPlan.class, planId);
            plan.days = WorkoutPlan.fromString(plan.dayString);
            List<Exercise> exercises = Day.fromString(plan.days.get(dayId).exerciseString);
            adapter.update(exercises);
        } else {
            WorkoutPlan plan = WorkoutPlan.findById(WorkoutPlan.class, planId);
            plan.eDays = WorkoutPlan.fromString(plan.eDayString);
            List<Exercise> exercises = Day.fromString(plan.eDays.get(dayId).exerciseString);
            adapter.update(exercises);
        }

    }
}
