package hu.bme.jurinmaxim.mobwebnhf.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import hu.bme.jurinmaxim.mobwebnhf.R;
import hu.bme.jurinmaxim.mobwebnhf.data.Exercise;


public class NewExerciseDialogFragment extends DialogFragment {

    public static final String TAG = "NewExerciseDialogFragment";
    String[][] muscleGroups = {
            {"Squat", "Leg press", "Lunge", "Deadlift", "Leg extension"},
            {"Lunge", "Deadlift", "Leg curl"},
            {"Standing calf raise", "Seated calf raise"},
            {"Bench press", "Chest fly", "Push-up"},
            {"Deadlift", "Pull-down", "Pull-up", "Bent-over row", "Back extension"},
            {"Upright row", "Shoulder press", "Shoulder fly", "Lateral raise"},
            {"Bench press", "Push-up", "Push-down", "Triceps extension"},
            {"Pull-down", "Pull-up", "Biceps curl"},
            {"Pull-down", "Pull-up", "Upright row"},
            {"Upright row", "Shoulder shrug"},
            {"Squat", "Crunch", "Russian twist"}};
    private NewExerciseDialogListener listener;
    private EditText setEditText;
    private EditText repEditText;
    private Spinner categorySpinner;
    private Spinner exerciseSpinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewExerciseDialogListener) {
            listener = (NewExerciseDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewExerciseDialogListener interface!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.new_exercise)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> listener.onExerciseCreated(getExercise()))
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private Exercise getExercise() {
        Exercise exercise = new Exercise();
        exercise.category = Exercise.Category.getByOrdinal(categorySpinner.getSelectedItemPosition());
        try {
            exercise.sets = Integer.parseInt(setEditText.getText().toString());
        } catch (NumberFormatException e) {
            exercise.sets = 0;
        }
        try {
            exercise.repetitions = Integer.parseInt(repEditText.getText().toString());
        } catch (NumberFormatException e) {
            exercise.repetitions = 0;
        }
        exercise.exercise = (String) exerciseSpinner.getSelectedItem();
        return exercise;
    }

    private View getContentView() {
        @SuppressLint("InflateParams") View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_exercise, null);
        setEditText = contentView.findViewById(R.id.SetEditText);
        repEditText = contentView.findViewById(R.id.RepEditText);
        categorySpinner = contentView.findViewById(R.id.ExerciseCategorySpinner);
        exerciseSpinner = contentView.findViewById(R.id.ExerciseSpinner);
        exerciseSpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, muscleGroups[0]));
        categorySpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.exercise_category_items)));
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                exerciseSpinner.setAdapter(new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, muscleGroups[position]));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return contentView;
    }

    public interface NewExerciseDialogListener {
        void onExerciseCreated(Exercise exercise);
    }


}


