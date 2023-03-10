package com.example.song4u.Util;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Errorcode extends AppCompatActivity {

    public static boolean isNumber(String str){
        if ( str  == null)
            return false;
        Pattern p = Pattern.compile("([\\p{Digit}]+)(([.]?)([\\p{Digit}]+))?");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static String ERROCODE(String code){
        String msg = "네트워크 및 서버 요청 실패 하였습니다.";
        if ( isNumber(code) == false)
            return msg;

        int mcode = Integer.valueOf(code);

        switch (mcode) {
            case 000:
                msg = "성공";
                break;

            case 1001:
                msg = "네트워크 연결 상태 확인 후 다시 시도해주세요.";
                break;

            case 100:
                msg = "필수 마라미터 에러";
                break;

            case 101:
                msg = "이미 참여한 음원입니다. 내일 다시 참여해주세요.";
                break;

            case 102:
                msg = "이미 참여한 음원입니다. 동일곡은 1시간당 1회 참여 가능합니다.";
                break;

            case 103:
                msg = "현재 잔여 재생횟수가 부족합니다.";
                break;

            case 200:
                msg = "회원가입 성공";
                break;

            case 201:
                msg = "로그인 성공";
                break;

            case 700:
                msg = "존재하지 않는 곡 정보입니다. 처음부터 다시 검색해주세요.";
                break;

            case 701:
                msg = "검색결과가 없습니다. 다른 제목으로 검색해주세요.";
                break;

            case 702:
                msg = "포인트가 부족하여 음원 등록을 할 수 없습니다.";
                break;

            case 703:
                msg = "이미 좋아요한 음원입니다.";
                break;

            case 704:
                msg = "이미 신고한 댓글입니다.";
                break;

            case 705:
                msg = "댓글이 수정되었습니다.";
                break;

            case 706:
                msg = "추천인 아이디가 존재하지 않습니다. 아이디 확인 후 다시 등록해주세요.";
                break;

            case 707:
                msg = "이미 추천인 아이디를 등록했습니다. 추천인 등록은 계정당 1회 가능합니다.";
                break;

            case 708:
                msg = "해당 추천인은 10회 추천인으로 등록되어있습니다. 최대 10회까지 추천인 적립이 가능합니다.";
                break;

            case 709:
                msg = "이미 등록되어 있는 음원입니다.";
                break;

            case 800:
                msg = "이미 가입된 기기입니다. 기기당 한 아이디만 가입 가능합니다.";
                break;

            case 801:
                msg = "사용중인 아이디입니다.";
                break;

            case 802:
                msg = "사용중인 닉네임입니다.";
                break;

            case 803:
                msg = "사용자ID 또는 패스워드가 맞지 않습니다.";
                break;

            case 804:
                msg = "사용자 정보가 없습니다.";
                break;

            case 805:
                msg = "아이디 / 패스워드를 확인 해주세요.";
                break;

            case 806:
                msg = "수정할 내용을 입력해 주세요.";
                break;

            case 807:
                msg = "이미지 업로드에 실패했습니다. 다른 사진을 선택해주세요.";
                break;

            case 808:
                msg = "사용자ID가 존재 합니다";
                break;

            case 809:
                msg = "사용자ID 혹은 패스워드를 확인 해주세요.";
                break;

            case 810:
                msg = "폰번호를 확인할 수 없습니다.";
                break;

            case 811:
                msg = "메뉴 이용에 적합하지 않은 기기입니다. 관리자에게 문의 부탁드립니다.";
                break;

            case 812:
                msg = "광고ID를 확인할 수 없습니다.";
                break;

            case 817:
                msg = "적립한 후원영상입니다.";
                break;

            case 818:
                msg = "포인트가 소진된 광고입니다.";
                break;

            case 820:
                msg = "비밀번호를 확인 해주세요.";
                break;

            case 822:
                msg = "잔액이 부족합니다.";
                break;

            case 823:
                msg = "회원가입 해주세요.";
                break;

            case 824:
                msg = "비밀번호를 입력 후 로그인을 해주세요.";
                break;

            case 825:
                msg = "적립내역이 없습니다. 광고에 1회이상 참여해야 출석체크가 완료됩니다.";
                break;

            case 826:
                msg = "회원탈퇴한 계정입니다. 다른계정으로 로그인해주세요.";
                break;

            case 827:
                msg = "상품을 구매할 수 없습니다. 네트워크 연결상태를 확인해주세요.";
                break;

            case 901:
                msg = "카카오톡으로 가입되어 있습니다.카카오톡 로그인을 해주세요.";
                break;

            case 902:
                msg = "구글 계정으로 가입되어 있습니다.구글로 로그인을 해주세요.";
                break;

            case 903:
                msg = "자신을 추천인으로 등록할 수 없습니다.";
                break;

            case 904:
                msg = "포인트가 부족합니다.";
                break;

            case 905:
                msg = "쿠폰이미지 생성에 실패하였습니다.";
                break;

            case 907:
                msg = "실패하였습니다.";
                break;

            case 908:
                msg = "마지막 페이지입니다.";
                break;

            case 909:
                msg = "첫번째 페이지입니다.";
                break;

            case 910:
                msg = "최신버젼입니다.";
                break;

            case 925:
                msg = "구글로 가입된 사용자입니다. 구글로 로그인해주세요.";
                break;

            case 926:
                msg = "페이스북으로 가입된 사용자입니다. 페이스북으로 로그인해주세요.";
                break;

            case 930:
                msg = "해당 아이디는 추천인 적립 이벤트가 종료되어 더이상 추천인을 받을 수 없습니다.\n마이페이지에서 다시 입력 가능합니다.";
                break;

            case 931:
                msg = "이미 출석보상을 획득하였습니다.";
                break;

            case 932:
                msg = "출석보상 서비스 기간이 아닙니다.";
                break;

            case 933:
                msg = "오늘의 기부포인트를 사용하셨습니다. 다음 출석일에 참여해 주시기 바랍니다.";
                break;

            case 950:
                msg = "자신을 구독 할 수 없습니다.";
                break;

            case 951:
                msg = "이미 구독중인 사용자 입니다.";
                break;

            case 952:
                msg = "자신을 차단 할 수 없습니다.";
                break;

            case 953:
                msg = "이미 차단한 사용자 입니다.";
                break;

            case 954:
                msg = "자신의 글을 공감 할 수 없습니다.";
                break;

            case 955:
                msg = "이미 공감한 글 입니다.";
                break;

            case 956:
                msg = "글을 작성한 사용자만 삭제가 가능합니다.";
                break;

            case 957:
                msg = "글을 작성한 사용자만 수정이 가능합니다.";
                break;

            case 958:
                msg = "컨텐츠가 없습니다.";
                break;

            case 959:
                msg = "글쓰기가 차단된 사용자 입니다. 관리자에게 문의 해주세요.";
                break;

            case 960:
                msg = "3분이후 글쓰기가 가능 합니다.";
                break;

            case 961:
                msg = "광고참여 가능 시간이 아닙니다.";
                break;

            case 962:
                msg = "삭제된 글입니다.";
                break;

            case 964:
                msg = "이미 적립한 포인트뉴스입니다.";
                break;

            case 965:
                msg = "쿠폰이 모두 소진되었습니다.";
                break;

            case 969:
                msg = "해당 상품의 하루 물량이 모두 판매되었습니다. \n내일 다시 구매를 이용해주세요.";
                break;

            case 970:
                msg = "최신버전으로 업데이트 후 참여 가능합니다.";
                break;

            case 971:
                msg = "이미 참여한 설문조사입니다.";
                break;

            case 972:
                msg = "스탬프가 부족합니다.";
                break;

            case 973:
                msg = "이미 신고접수된 글 입니다.";
                break;

            case 997:
                msg = "적립한 광고입니다.";
                break;

            case 999:
                msg = "좋아요 참여한 댓글입니다.";
                break;


        }

        return msg;
    }

}
