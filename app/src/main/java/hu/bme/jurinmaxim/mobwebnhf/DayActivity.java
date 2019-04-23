package hu.bme.jurinmaxim.mobwebnhf;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hu.bme.jurinmaxim.mobwebnhf.adapter.DayAdapter;
import hu.bme.jurinmaxim.mobwebnhf.data.Day;
import hu.bme.jurinmaxim.mobwebnhf.data.WorkoutPlan;
import hu.bme.jurinmaxim.mobwebnhf.fragment.NewDayDialogFragment;

public class DayActivity extends AppCompatActivity
        implements NewDayDialogFragment.NewDayDialogListener,
        DayAdapter.DayClickListener {

    public static ArrayList<String> daysLeft;
    private DayAdapter adapter;
    private long planId;
    private int Id = 0;
    private boolean odd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        daysLeft = new ArrayList<>();
        for (Day.DayOfWeek dow : Day.DayOfWeek.values())
            daysLeft.add(dow.name());


        Intent intent = getIntent();
        String title = intent.getStringExtra("name");
        planId = intent.getLongExtra("id", 0);
        odd = intent.getBooleanExtra("parity", true);
        setTitle(title);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> new NewDayDialogFragment().show(getSupportFragmentManager(), NewDayDialogFragment.TAG));

        RecyclerView recyclerView = findViewById(R.id.DayRecyclerView);
        adapter = new DayAdapter(this, this);
        loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // if(odd){
        if (adapter.days.size() > 0) {
            for (int i = 0; i < adapter.days.size(); i++) {
                for (int j = 0; i < daysLeft.size(); j++) {
                    if (adapter.days.get(i).dayOfWeek.name().equals(daysLeft.get(j))) {
                        daysLeft.remove(j);
                        break;
                    }
                }
            }
        }
     /*   }else {
            if (adapter.days.size() > 0) {
                for (int i = 0; i < adapter.days.size(); i++) {
                    for (int j = 0; i < daysLeft.size(); j++) {
                        if (adapter.days.get(i).dayOfWeek.name().equals(daysLeft.get(j))) {
                            daysLeft.remove(j);
                            break;
                        }
                    }
                }
            }

        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_day, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toggle:
                WorkoutPlan workoutPlan = WorkoutPlan.findById(WorkoutPlan.class, planId);
                if (workoutPlan.category.equals(WorkoutPlan.Category.TwoWeek)) {
                    if (odd)
                        Toast.makeText(this, "Even week selected", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, "Odd week selected", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, DayActivity.class);
                    intent.putExtra("name", workoutPlan.name);
                    intent.putExtra("id", workoutPlan.getId());
                    intent.putExtra("parity", !odd);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    DayActivity.this.startActivity(intent);
                } else {
                    Toast.makeText(this, "This is a One Week plan!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDayDeleted(Day day) {
        if (odd) {
            adapter.deleteDay(day);
            WorkoutPlan workoutPlan = WorkoutPlan.findById(WorkoutPlan.class, planId);
            workoutPlan.days = adapter.days;
            workoutPlan.dayString = workoutPlan.toString(workoutPlan.days);
            workoutPlan.save();
        } else {
            adapter.deleteDay(day);
            WorkoutPlan workoutPlan = WorkoutPlan.findById(WorkoutPlan.class, planId);
            workoutPlan.eDays = adapter.days;
            workoutPlan.eDayString = workoutPlan.toString(workoutPlan.eDays);
            workoutPlan.save();
        }

    }

    @Override
    public void onDayCreated(Day day) {
        if (odd) {
            adapter.addDay(day);
            day.planID = planId;
            day.dayId = Id++;
            day.parity = true;
            WorkoutPlan workoutPlan = WorkoutPlan.findById(WorkoutPlan.class, planId);
            if (workoutPlan.days == null)
                workoutPlan.days = new ArrayList<>();
            workoutPlan.days = adapter.days;
            workoutPlan.dayString = workoutPlan.toString(workoutPlan.days);
            workoutPlan.save();
        } else {
            adapter.addDay(day);
            day.planID = planId;
            day.dayId = Id++;
            day.parity = false;
            WorkoutPlan workoutPlan = WorkoutPlan.findById(WorkoutPlan.class, planId);
            if (workoutPlan.eDays == null)
                workoutPlan.eDays = new ArrayList<>();
            workoutPlan.eDays = adapter.days;
            workoutPlan.eDayString = workoutPlan.toString(workoutPlan.eDays);
            workoutPlan.save();
        }
    }


    private void loadItemsInBackground() {
        if (odd) {
            List<Day> days = WorkoutPlan.fromString(WorkoutPlan.findById(WorkoutPlan.class, planId).dayString);
            adapter.update(days);
        } else {
            List<Day> eDays = WorkoutPlan.fromString(WorkoutPlan.findById(WorkoutPlan.class, planId).eDayString);
            adapter.update(eDays);
        }

    }

}