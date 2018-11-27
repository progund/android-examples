package se.juneday.memberimages;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import se.juneday.memberimages.domain.Member;

public class MemberAdapter extends ArrayAdapter<Member> implements View.OnClickListener {

    private static final String LOG_TAG = MemberAdapter.class.getCanonicalName();
    private List<Member> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView nameView;
        TextView emailView;

        ImageView avatarView;
    }

    public MemberAdapter(List<Member> data, Context context) {
        super(context, R.layout.member_row, data);
        this.dataSet = data;
        this.mContext=context;

    }

    @Override
    public void onClick(View v) {

        int position=(Integer) v.getTag();
        Object object= getItem(position);
        Member member = (Member)object;

        Log.d(LOG_TAG, "Release date " + member.name());
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Member member= getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.member_row, parent, false);
            viewHolder.nameView = (TextView) convertView.findViewById(R.id.name);
            viewHolder.emailView = (TextView) convertView.findViewById(R.id.email);
            viewHolder.avatarView = (ImageView) convertView.findViewById(R.id.avatar);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        lastPosition = position;

        viewHolder.nameView.setText(member.name());
        viewHolder.emailView.setText(member.email());

        Bitmap bitmap = Utils.avatarBitmap(mContext, member);
        if (bitmap != null) {
            Log.d(LOG_TAG, "  using existing file for " + member.name());
            viewHolder.avatarView.setImageBitmap(bitmap);
        }

        return convertView;

    }
}
