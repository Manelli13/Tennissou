package elite.nation.tenissou;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by nicolas on 21/02/2018.
 */


public class MatchAdapter extends BaseAdapter {


    //String [] result;
    Context context;
    //int [] imageId;
    ArrayList<Match> mAMatch;
   // Match mMatch;
    private static LayoutInflater inflater=null;


    public MatchAdapter(Choose_match_activity choose_match_activity, ArrayList<Match> match) {

        // TODO Auto-generated constructor stub

        context=choose_match_activity;
        mAMatch = match;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mAMatch.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.adapter_match, null);
        holder.tv=(TextView) rowView.findViewById(R.id.adapter_name_txt);
        holder.img=(ImageView) rowView.findViewById(R.id.adapter_image_imv);
        holder.tv.setText(mAMatch.get(position).getmNameMatch());
        holder.img.setImageBitmap((mAMatch.get(position).getmStade()));
      /*  rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+mAMatch.get(position), Toast.LENGTH_LONG).show();
            }
        });*/
        return rowView;
    }

}