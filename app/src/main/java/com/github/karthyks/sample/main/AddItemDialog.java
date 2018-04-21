package com.github.karthyks.sample.main;


import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.karthyks.sample.R;

public class AddItemDialog extends Dialog implements View.OnClickListener {

  private EditText etName;
  private EditText etAddress;
  private EditText etMobile;
  private IMainPresenter mainPresenter;

  AddItemDialog(@NonNull Context context, IMainPresenter mainPresenter) {
    super(context);
    this.mainPresenter = mainPresenter;
    setContentView(R.layout.dialog_add_item);
    etName = findViewById(R.id.etName);
    etAddress = findViewById(R.id.etAddress);
    etMobile = findViewById(R.id.etMobile);
    Button btnSave = findViewById(R.id.btn_save);
    btnSave.setOnClickListener(this);
  }

  @Override
  public void onClick(View v) {
    switch (v.getId()) {
      case R.id.btn_save:
        if (TextUtils.isEmpty(etName.getText())
            || TextUtils.isEmpty(etAddress.getText())
            || TextUtils.isEmpty(etMobile.getText())) {
          Toast.makeText(getContext(), "Fields cannot be empty!", Toast.LENGTH_LONG).show();
        } else {
          mainPresenter.addItem(etName.getText().toString(), etAddress.getText().toString(),
              etMobile.getText().toString());
          clearFields();
        }
        break;
      default:
    }
  }

  private void clearFields() {
    etName.setText("");
    etAddress.setText("");
    etMobile.setText("");
  }
}
