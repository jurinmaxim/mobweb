package hu.bme.jurinmaxim.mobwebnhf;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import hu.bme.jurinmaxim.mobwebnhf.adapter.WorkoutPlanAdapter;
import hu.bme.jurinmaxim.mobwebnhf.data.WorkoutPlan;
import hu.bme.jurinmaxim.mobwebnhf.fragment.NewWorkoutPlanDialogFragment;

public class MainActivity extends AppCompatActivity
        implements NewWorkoutPlanDialogFragment.NewWorkoutPlanDialogListener,
        WorkoutPlanAdapter.WorkoutPlanClickListener {

    public WorkoutPlanAdapter adapter = new WorkoutPlanAdapter(this, this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> new NewWorkoutPlanDialogFragment().show(getSupportFragmentManager(), NewWorkoutPlanDialogFragment.TAG));


        RecyclerView recyclerView = findViewById(R.id.MainRecyclerView);
        loadItemsInBackground();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWorkoutPlanDeleted(WorkoutPlan plan) {
        adapter.deletePlan(plan);
        plan.delete();
    }

    @Override
    public void onWorkoutPlanCreated(WorkoutPlan plan) {
        adapter.addPlan(plan);
        plan.save();
    }

    private void loadItemsInBackground() {
        List<WorkoutPlan> workoutPlans = WorkoutPlan.listAll(WorkoutPlan.class);
        adapter.update(workoutPlans);

    }
}
