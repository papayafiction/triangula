package de.sopamo.triangula.android.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import de.sopamo.triangula.android.LevelChooserActivity;
import de.sopamo.triangula.android.R;
import de.sopamo.triangula.android.levels.Level;
import de.sopamo.triangula.android.levels.OnlineLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class LevelAdapter extends BaseAdapter {

    private List<Level> levelList = new ArrayList<Level>();
    private LayoutInflater inflater;

    public LevelAdapter(Activity activity,List<Level> levelList) {
        this.levelList = levelList;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return levelList.size();
    }

    @Override
    public Level getItem(int position) {
        return levelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
            View vi=convertView;
            if(convertView==null)
                vi = inflater.inflate(R.layout.level_list_item, null);

            TextView title = (TextView)vi.findViewById(R.id.list_item_text); // title
            Button button = (Button) vi.findViewById(R.id.list_item_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LevelChooserActivity.level = getItem(position);
                    if(LevelChooserActivity.level instanceof OnlineLevel) {
                        ((OnlineLevel) LevelChooserActivity.level).load().start();
                    } else {
                        LevelChooserActivity.startGame();
                    }
                }
            });
            title.setText(""+position);
            return vi;
    }
}
