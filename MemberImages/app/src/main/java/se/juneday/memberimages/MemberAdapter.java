package se.juneday.memberimages;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import se.juneday.memberimages.domain.Member;

public class MemberAdapter extends ArrayAdapter<Member> {

    private static final String LOG_TAG = MemberAdapter.class.getCanonicalName();

    private List<Member> members;
    private Context context;

    // Used to cache View
    private static class ViewHolder {
        TextView nameView;
        TextView emailView;
        ImageView avatarView;
    }

    public MemberAdapter(List<Member> members, Context context) {
        super(context, R.layout.member_row, members);
        this.members = members;
        this.context = context;
    }


    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get member at position
        Member member= getItem(position);

        ViewHolder viewHolder;

        // Reused view or not
        if (convertView == null) {

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.member_row, parent, false);
            viewHolder.nameView = (TextView) convertView.findViewById(R.id.name);
            viewHolder.emailView = (TextView) convertView.findViewById(R.id.email);
            viewHolder.avatarView = (ImageView) convertView.findViewById(R.id.avatar);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        lastPosition = position;



        // Set ViewHolder variables
        viewHolder.nameView.setText(member.name());
        viewHolder.emailView.setText(member.email());
        Bitmap bitmap = Utils.avatarBitmap(context, member);
        if (bitmap != null) {
            Log.d(LOG_TAG, "  using existing file for " + member.name());
            viewHolder.avatarView.setImageBitmap(bitmap);
        } else {
            Log.d(LOG_TAG, "  using default resource for bitmap");
            viewHolder.avatarView.setImageResource(R.drawable.ic_launcher_background);
        }

        return convertView;
    }
}
