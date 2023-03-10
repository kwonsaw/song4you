package com.example.song4u.Adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.song4u.R;
import com.example.song4u.Util.GlideImageGetter;
import com.example.song4u.network.resultmodel.GetNoticeDataResultModel;

import java.util.ArrayList;

public class NoticeAdapter extends BaseExpandableListAdapter {

    private final Context mContext;
    private ArrayList<GetNoticeDataResultModel> mCountries;

    public NoticeAdapter(Context context, ArrayList<GetNoticeDataResultModel> list) {

        this.mContext = context;
        this.mCountries = list;

    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GetNoticeDataResultModel content = mCountries.get(groupPosition);
        View v = convertView;
        Context context = parent.getContext();

        //convertView가 비어있을 경우 xml파일을 inflate 해줌
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item_notice, parent, false);
        }

        String datestr = content.getInsertedDatetime();
        datestr = datestr.substring(datestr.length()-9, datestr.length());

        //View들은 반드시 아이템 레이아웃을 inflate한 뒤에 작성할 것
        ImageView arrowIcon = (ImageView) v.findViewById(R.id.mImg);
        TextView tTextView = (TextView) v.findViewById(R.id.mText);
        TextView dTextView = (TextView) v.findViewById(R.id.dateText);

        //그룹 펼쳐짐 여부에 따라 아이콘 변경
        if (isExpanded) {
            arrowIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24);
        } else {
            arrowIcon.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24);
        }
        //리스트 아이템의 내용 설정
        if (content.getType().equalsIgnoreCase("n")) {
            tTextView.setText("[공지] "+content.getNoticeTitle());
        } else {
            tTextView.setText("[이벤트] "+content.getNoticeTitle());
        }


        String test1 = content.getInsertedDatetime().split(" ")[0];
        dTextView.setText(test1);

        return v;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        GetNoticeDataResultModel content = mCountries.get(groupPosition);
        View v = convertView;
        Context context = parent.getContext();

        if (v == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.list_item_child_notice, parent, false);

        }

        TextView mTextView = (TextView) v.findViewById(R.id.mText);
        /* textview에 html img 태그 적용 */
        GlideImageGetter getter = new GlideImageGetter(context, mTextView);
        Spanned htmlSpan = Html.fromHtml(content.getNoticeDescription(), getter, null);
        mTextView.setText(htmlSpan);


        return v;
   }



    @Override
    public boolean hasStableIds() {
        //TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getGroupCount() {
        return mCountries.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mCountries.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
     return mCountries.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


}
