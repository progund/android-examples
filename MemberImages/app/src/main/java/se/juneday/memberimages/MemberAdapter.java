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

    // String tag for logging
    private static final String LOG_TAG = MemberAdapter.class.getCanonicalName();

    // Our model or data, a list of members
    private List<Member> members;

    // Context, needed to find views etc
    private Context context;

    // Used to cache View
    // Check out: http://www.vogella.com/tutorials/AndroidListView/article.html
    private static class ViewHolder {
        TextView nameView;
        TextView emailView;
        ImageView avatarView;
    }

    /**
     * Creates a MemberAdapter
     * @param members members to create Views from
     * @param context used to find views etc
     */
    public MemberAdapter(List<Member> members, Context context) {
        super(context, R.layout.member_row, members);
        this.members = members;
        this.context = context;
    }


    private int lastPosition = -1;

    /**
     * Get the View (for a Member)
     * @param position the position/index in the list of members
     * @param convertView old view to reuse
     * @param parent The parent view this View will be attached to
     * @return a View representing the Member at index (position) .. to put in the ListView
     * https://developer.android.com/reference/android/widget/Adapter.html#getView(int,%20android.view.View,%20android.view.ViewGroup)
     */
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
