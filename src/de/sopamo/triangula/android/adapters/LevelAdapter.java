package de.sopamo.triangula.android.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import de.sopamo.triangula.android.App;
import de.sopamo.triangula.android.R;
import de.sopamo.triangula.android.StartLevelService;
import de.sopamo.triangula.android.levels.BaseOfficialLevel;
import de.sopamo.triangula.android.levels.Level;

import java.util.ArrayList;
import java.util.List;

public class LevelAdapter extends BaseAdapter {

    private List<Level> levelList = new ArrayList<Level>();
    private LayoutInflater inflater;
    private Context mContext;

    public LevelAdapter(Context context,List<Level> levelList) {
        App.setLevelList(levelList);
        this.levelList = levelList;
        this.mContext=context;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

            final Level l = levelList.get(position);
            TextView title = (TextView)vi.findViewById(R.id.list_item_text); // title
            Button button = (Button) vi.findViewById(R.id.list_item_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent startLevelIntent = new Intent(mContext, StartLevelService.class);
                    startLevelIntent.putExtra("level", l);
                    mContext.startService(startLevelIntent);
                }
            });
            if(l instanceof BaseOfficialLevel) {
                title.setText(l.getLevelName());
            } else {
                title.setText(l.getCreatorTag()+": "+l.getLevelName());
            }

            return vi;
    }



}
