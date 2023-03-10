package com.example.song4u.Fragment;

import static com.example.song4u.activity.Season2BaseActivity.getVersion;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.song4u.Adapter.CouponRecyclerAdapter;
import com.example.song4u.Adapter.StoreRecyclerAdapter;
import com.example.song4u.R;
import com.example.song4u.Util.CommonUtil;
import com.example.song4u.Util.Errorcode;
import com.example.song4u.activity.Season2BaseActivity;
import com.example.song4u.activity.StoreActivity;
import com.example.song4u.databinding.FragmentStoreBinding;
import com.example.song4u.network.NetworkManager;
import com.example.song4u.network.resultmodel.GetCouponDataResultModel;
import com.example.song4u.network.resultmodel.GetCouponResultModel;
import com.example.song4u.network.resultmodel.GetGifticonDataResultModel;
import com.example.song4u.network.resultmodel.GetGifticonResultModel;
import com.example.song4u.network.resultmodel.SetGifticonResultModel;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Store extends Fragment implements View.OnClickListener, AbsListView.OnScrollListener{

    private FragmentStoreBinding binding;

    private GetGifticonResultModel getGifticonResultModel;
    private GetCouponResultModel getCouponResultModel;

    private ArrayList<GetGifticonDataResultModel> storeList;
    private ArrayList<GetCouponDataResultModel> couponList;

    /* 스토어 상품 리스트 */
    private RecyclerView sRecyclerView;
    private StoreRecyclerAdapter storeRecyclerAdapter;

    /* 쿠폰함 리스트 */
    private RecyclerView cRecyclerView;
    private CouponRecyclerAdapter couponRecyclerAdapter;

    private int PageNum = 1;
    private int Totalpage = 1;
    private boolean loock = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentStoreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowCustomEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        getGifticon();

        return root;
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void init() {


        sRecyclerView = binding.sRecyclerView;
        cRecyclerView = binding.cRecyclerView;

        binding.storebtn.setSelected(true);

        binding.storebtn.setOnClickListener(this);
        binding.couponbtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.storebtn:

                binding.subText.setVisibility(View.GONE);
                binding.sRecyclerView.setVisibility(View.VISIBLE);
                sRecyclerView.setVisibility(View.VISIBLE);
                cRecyclerView.setVisibility(View.GONE);
                binding.storebtn.setSelected(true);
                binding.couponbtn.setSelected(false);

                getGifticon();

                break;
            case R.id.couponbtn:

                PageNum = 1;

                binding.couponbtn.setSelected(true);
                binding.storebtn.setSelected(false);
                sRecyclerView.setVisibility(View.GONE);
                cRecyclerView.setVisibility(View.VISIBLE);

                getCoupon(PageNum);
                break;

        }
    }

    public void setStoreData(List<GetGifticonDataResultModel> list) {

        storeList = new ArrayList<>();
        storeList.addAll(list);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);

        storeRecyclerAdapter = new StoreRecyclerAdapter(getActivity(),storeList);

        sRecyclerView.setAdapter(storeRecyclerAdapter);
        sRecyclerView.setLayoutManager(gridLayoutManager);

        storeRecyclerAdapter.setOnItemClickListener(new StoreRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                Intent i = new Intent(getActivity(), StoreActivity.class);
                //i.putStringArrayListExtra("storeinfo", storeList);
                //i.putParcelableArrayListExtra("storeinfo", storeList.get(position));
                i.putExtra("storeinfo", storeList);
                i.putExtra("position",position);
                startActivity(i);

            }
        }) ;

    }

    public void setCouponData(List<GetCouponDataResultModel> list) {

        if ( PageNum == 1){
            couponList = new ArrayList<>();
            couponList.addAll(list);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false);

            couponRecyclerAdapter = new CouponRecyclerAdapter(getActivity(),couponList);

            cRecyclerView.setAdapter(couponRecyclerAdapter);
            cRecyclerView.setLayoutManager(gridLayoutManager);

        }else {
            couponList.addAll(list);
            couponRecyclerAdapter.notifyDataSetChanged();
        }



        couponRecyclerAdapter.setOnItemClickListener(new CouponRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {

                setImgPopupN(couponList, position);

            }
        }) ;

        cRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // 리사이클러뷰 가장 마지막 index
                int lastPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                // 받아온 리사이클러 뷰 카운트
                int totalCount = recyclerView.getAdapter().getItemCount();
                // 스크롤을 맨 끝까지 한 것!

                if(lastPosition == totalCount -1 && loock == false && PageNum < Totalpage){
                    loock = true;
                    getCoupon(++PageNum);

                }

            }
        });

    }

    /**
     * 구매 쿠폰 이미지 보여주는 팝업
     */
    public void setImgPopupN(List<GetCouponDataResultModel> list, int pos){
        View i = LayoutInflater.from(getActivity()).inflate(R.layout.cuponpopupview, null);
        ImageView cancel = (ImageView)i.findViewById(R.id.store_img_close);
        TextView title = (TextView)i.findViewById(R.id.store_gift_title);
        TextView brand = (TextView)i.findViewById(R.id.store_gift_brand);
        ImageView img = (ImageView)i.findViewById(R.id.store_gift_img);
        TextView datetime = (TextView)i.findViewById(R.id.store_gift_datetime);
        TextView date = (TextView)i.findViewById(R.id.store_gift_date);
        TextView PIN = (TextView)i.findViewById(R.id.store_gift_pin);
        ImageView Barcode = (ImageView)i.findViewById(R.id.store_gift_barcode);
        Button copybtn = (Button)i.findViewById(R.id.copyBtn);

        //util.setGlobalFont(getApplicationContext(), i);

        final Dialog ImgDialog = new Dialog(getActivity());
        ImgDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ImgDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ImgDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ImgDialog.setContentView(i);
        ImgDialog.setCanceledOnTouchOutside(false);
        ImgDialog.setCancelable(true);

        ViewGroup.LayoutParams params = ImgDialog.getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        ImgDialog.getWindow().setAttributes((WindowManager.LayoutParams) params);

        title.setText(list.get(pos).getProductname());
        datetime.setText("구매일자 : "+list.get(pos).getInsertedDatetime());
        brand.setText(list.get(pos).getBrandname());
        date.setText("유효기간 : " + list.get(pos).getExdate());
        PIN.setText("핀번호 : " + list.get(pos).getEpin());

        Glide.with(getActivity())
                .load( list.get(pos).getGifticonImageUrl())
                //.error(R.mipmap.profiledefault)
                .into(img);
        //util.ImageDownLoad(img, content.getImageUrl(), getApplicationContext(), 0);

        if (list.get(pos).getEpin() != null ) {
            String strBarcode = list.get(pos).getEpin().replaceAll("-", "").replaceAll("/" , "");
            Bitmap barcode = createBarcode(strBarcode);
            Barcode.setImageBitmap(barcode);
            Barcode.invalidate();
        }


        copybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copy(list.get(pos).getEpin());
            }});

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImgDialog.dismiss();
            }});
        ImgDialog.show();
    }

    /** 바코드 생성 */
    public Bitmap createBarcode(String code){
        Bitmap bitmap =null;
        MultiFormatWriter gen = new MultiFormatWriter();
        try {
            final int WIDTH = 840;
            final int HEIGHT = 160;
            BitMatrix bytemap = gen.encode(code, BarcodeFormat.CODE_128, WIDTH, HEIGHT);
            bitmap = Bitmap.createBitmap(WIDTH, HEIGHT, Bitmap.Config.ARGB_8888);
            for (int i = 0 ; i < WIDTH ; ++i)
                for (int j = 0 ; j < HEIGHT ; ++j) {
                    bitmap.setPixel(i, j, bytemap.get(i,j) ? Color.BLACK : Color.WHITE);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /** 클립보드 복사 **/
    public void copy(String getText) {
        // 클립보드 객체 얻기
        ClipboardManager clipboardManager = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        // 클립데이터 생성
        ClipData clipData = ClipData.newPlainText("핀번호", getText); //Test 가 실질적으로 복사되는 Text
        // 클립보드에 추가
        clipboardManager.setPrimaryClip(clipData);

        ((Season2BaseActivity) getActivity()).showToast("핀번호가 복사되었습니다.");

    }

    /**
     /* 스토어 상품
     **/

    public void getGifticon(){

        NetworkManager manager = new NetworkManager();
        manager.getGifticon(getVersion(), CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), null, "guest"), new Callback<GetGifticonResultModel>() {
            @Override
            public void onResponse(Call<GetGifticonResultModel> call, Response<GetGifticonResultModel> response) {
                getGifticonResultModel = response.body();

                if (response.isSuccessful()) {

                    if (getGifticonResultModel.getResultCode().equalsIgnoreCase("200")) {
                        setStoreData(getGifticonResultModel.getList());
                    }

                } else {
                    ((Season2BaseActivity) getActivity()).showToast(Errorcode.ERROCODE("1001"));
                }
            }

            @Override
            public void onFailure(Call<GetGifticonResultModel> call, Throwable t) {
                ((Season2BaseActivity) getActivity()).showToast(Errorcode.ERROCODE("1001"));
            }
        });

    }


    /**
     /* 쿠폰함 상품
     **/

    public void getCoupon(int pagenum){

        String page = String.valueOf(pagenum);

        NetworkManager manager = new NetworkManager();
        manager.getCoupon(getVersion(), CommonUtil.mSharePrefreences(getActivity(), getString(R.string.share_userId), null, "guest"), page, new Callback<GetCouponResultModel>() {
            @Override
            public void onResponse(Call<GetCouponResultModel> call, Response<GetCouponResultModel> response) {
                getCouponResultModel = response.body();

                if (response.isSuccessful()) {

                    if (getCouponResultModel.getResultCode().equalsIgnoreCase("200")) {

                        if (getCouponResultModel.getList().size() == 0) {
                            binding.subText.setVisibility(View.VISIBLE);
                            binding.sRecyclerView.setVisibility(View.GONE);
                        } else {

                            binding.subText.setVisibility(View.GONE);

                            loock = false;
                            Totalpage = Integer.parseInt(getCouponResultModel.getTotalpage());
                            setCouponData(getCouponResultModel.getList());

                        }

                    }

                } else {
                    ((Season2BaseActivity) getActivity()).showToast(Errorcode.ERROCODE("1001"));
                }
            }

            @Override
            public void onFailure(Call<GetCouponResultModel> call, Throwable t) {
                ((Season2BaseActivity) getActivity()).showToast(Errorcode.ERROCODE("1001"));
            }
        });

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        int count = totalItemCount - visibleItemCount;
        if (firstVisibleItem >= count && totalItemCount != 0 && loock == false && PageNum < Totalpage) {
            loock = true;
            Log.e("kwonsaw","onScroll");


        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {}

}