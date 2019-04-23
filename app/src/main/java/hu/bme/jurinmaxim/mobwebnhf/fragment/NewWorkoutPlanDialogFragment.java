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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import hu.bme.jurinmaxim.mobwebnhf.R;
import hu.bme.jurinmaxim.mobwebnhf.data.WorkoutPlan;


public class NewWorkoutPlanDialogFragment extends DialogFragment {

    public static final String TAG = "NewWorkoutPlanDialogFragment";
    private NewWorkoutPlanDialogListener listener;
    private EditText nameEditText;
    private Spinner categorySpinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewWorkoutPlanDialogListener) {
            listener = (NewWorkoutPlanDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewWorkoutPlanDialogListener interface!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.new_workout_plan)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> {
                    if (isValid()) {
                        listener.onWorkoutPlanCreated(getWorkoutPlan());
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }


    private boolean isValid() {
        return nameEditText.getText().length() > 0;
    }

    private WorkoutPlan getWorkoutPlan() {
        WorkoutPlan workoutPlan = new WorkoutPlan();
        workoutPlan.name = nameEditText.getText().toString();
        workoutPlan.category = WorkoutPlan.Category.getByOrdinal(categorySpinner.getSelectedItemPosition());
        return workoutPlan;
    }

    private View getContentView() {
        @SuppressLint("InflateParams") View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_workout_plan, null);
        nameEditText = contentView.findViewById(R.id.WorkoutPlanNameEditText);
        categorySpinner = contentView.findViewById(R.id.WorkoutPlanCategorySpinner);
        categorySpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.workout_category_items)));
        return contentView;
    }


    public interface NewWorkoutPlanDialogListener {
        void onWorkoutPlanCreated(WorkoutPlan plan);
    }
}


