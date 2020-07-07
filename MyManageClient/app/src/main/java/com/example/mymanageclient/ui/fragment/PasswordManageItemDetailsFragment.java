package com.example.mymanageclient.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.classic.adapter.BaseAdapterHelper;
import com.classic.adapter.CommonAdapter;
import com.example.mymanageclient.R;
import com.example.mymanageclient.tool.HttpUtils;
import com.example.mymanageclient.tool.StrUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import pojo.IconDetails;
import pojo.UserItem;
import web.WebResult;

public final class PasswordManageItemDetailsFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.pwd_ItemDetailsId)      EditText          itemEdit;
    @BindView(R.id.pwd_AddressDetailsId)   EditText          addressEdit;
    @BindView(R.id.pwd_UserNameDetailsId)  EditText          userNameEdit;
    @BindView(R.id.pwd_PasswordDetailsId)  EditText          passwordEdit;
    @BindView(R.id.pwd_RemarkDetailsId)    EditText          remarkEdit;
    @BindView(R.id.pwd_ManageDet_Ok_BtnId) Button            okBtn;
    @BindView(R.id.types)                  Spinner           types;
    private                                UserItem          userItem;
    private                                Unbinder          bind;
    private                                boolean           isInited = false;
    private                                List<IconDetails> passwordTypeList;

    public PasswordManageItemDetailsFragment(UserItem userItem, String title) {
        Bundle bundle = new Bundle();
        if (userItem != null) {
            bundle.putString("UserItem", new Gson().toJson(userItem));
        }
        bundle.putString("title", title);
        setArguments(bundle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.password_manage_item_details, container, false);
        bind = ButterKnife.bind(this, v);

        initPasswordType();

        return v;
    }

    /**
     * 初始化类型列表
     */
    private void initPasswordType() {
        HttpUtils.get(getActivity(), getString(R.string.baseURL) + "/icon?type=1", webResult -> {
            if (webResult.getState() == WebResult.OK){
                passwordTypeList = (ArrayList<IconDetails>) webResult.getData();
                types.setAdapter(new CommonAdapter<IconDetails>(getActivity(), android.R.layout.simple_list_item_1, passwordTypeList) {
                    @Override
                    public void onUpdate(BaseAdapterHelper helper, IconDetails item, int position) {
                        helper.setText(android.R.id.text1, item.getName());
                    }
                });
            }
            loadParameters();
        }, new IconDetails());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除绑定
        bind.unbind();
    }

    @OnTextChanged({R.id.pwd_ItemDetailsId, R.id.pwd_AddressDetailsId, R.id.pwd_UserNameDetailsId
                           , R.id.pwd_PasswordDetailsId, R.id.pwd_RemarkDetailsId})
    void onTextChanged() {
        if (isInited)
            okBtn.setEnabled(true);
    }


    /**
     * 读取上层窗口传递的参数
     */
    private void loadParameters() {
        if (getArguments() != null && getArguments().getString("UserItem") != null) {
            userItem = new Gson().fromJson(getArguments().getString("UserItem"), new TypeToken<UserItem>() {
            }.getType());
        }
        String title = getArguments().getString("title");
        if (userItem == null) {
            userItem = new UserItem();
            int i = passwordTypeList.stream().map(IconDetails::getName).collect(Collectors.toList()).indexOf(title);
            types.setSelection(i);
        } else {
            for (int i = 0; i < passwordTypeList.size(); i++) {
                if (passwordTypeList.get(i).getId() == userItem.getTypeNameId()) {
                    types.setSelection(i);
                    break;
                }
            }
            itemEdit.setText(userItem.getItemName());
            addressEdit.setText(userItem.getAddress());
            userNameEdit.setText(userItem.getUserName());
            passwordEdit.setText(userItem.getPassword());
            remarkEdit.setText(userItem.getRemark());
        }
        isInited = true;
    }

    @OnClick(R.id.pwd_ManageDet_Ok_BtnId)
    public void onClick(View view) {
        userItem.setItemName(itemEdit.getText().toString());
        userItem.setAddress(addressEdit.getText().toString());
        userItem.setUserName(userNameEdit.getText().toString());
        userItem.setPassword(passwordEdit.getText().toString());
        userItem.setRemark(remarkEdit.getText().toString());
        userItem.setSalt(StrUtils.getRandomString(getResources().getInteger(R.integer.defaultSaltLength)));
        userItem.setTypeNameId(passwordTypeList.get(types.getSelectedItemPosition()).getId());
        // TODO: 2020/7/6 修改逻辑
//        if (DbHelper.getInstance().saveUserItem(userItem)) {
//            PageUtils.showMessage(getContext(), "修改成功！");
//        }

        getActivity().onBackPressed();
    }

}
