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
import android.widget.Spinner;

import hu.bme.jurinmaxim.mobwebnhf.DayActivity;
import hu.bme.jurinmaxim.mobwebnhf.R;
import hu.bme.jurinmaxim.mobwebnhf.data.Day;


public class NewDayDialogFragment extends DialogFragment {

    public static final String TAG = "NewDayDialogFragment";
    private NewDayDialogListener listener;
    private Spinner categorySpinner;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentActivity activity = getActivity();
        if (activity instanceof NewDayDialogListener) {
            listener = (NewDayDialogListener) activity;
        } else {
            throw new RuntimeException("Activity must implement the NewDayDialogListener interface!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle(R.string.new_day)
                .setView(getContentView())
                .setPositiveButton(R.string.ok, (dialogInterface, i) -> listener.onDayCreated(getDay()))
                .setNegativeButton(R.string.cancel, null)
                .create();
    }


    private Day getDay() {
        Day day = new Day();
        day.dayOfWeek = Day.DayOfWeek.enumFromString(DayActivity.daysLeft.get(categorySpinner.getSelectedItemPosition()));
        DayActivity.daysLeft.remove(DayActivity.daysLeft.get(categorySpinner.getSelectedItemPosition()));
        return day;
    }

    private View getContentView() {
        @SuppressLint("InflateParams") View contentView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_new_day, null);
        categorySpinner = contentView.findViewById(R.id.DayCategorySpinner);
        categorySpinner.setAdapter(new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_dropdown_item, DayActivity.daysLeft));
        return contentView;
    }


    public interface NewDayDialogListener {
        void onDayCreated(Day day);
    }
}


