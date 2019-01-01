package com.example.anton.syshelper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.jraska.console.Console;

public class ConsoleFragment extends AbstractFragment{
    private static final int LAYOUT=R.layout.fragment_console;
    private EditText editText;
    private TextView commandLog;
    private StringBuilder currienttext;
    private Button sendButton;
    private ServerInfoActivity activity;

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.send_button:
                    hideKeyboard(getActivity());
                    activity.execCommand(editText.getText().toString());
                    editText.setText("");
                    break;
            }
        }
    };

    protected void addLog(String s){

        currienttext=new StringBuilder(s.replace("$","\n")+"\n"+currienttext.toString());
        commandLog.setText(currienttext);
    }


    public void setContext(Context context) {
        this.context = context;
    }

    public static ConsoleFragment getInstance(Context context) {
        Bundle args = new Bundle();
        ConsoleFragment fragment=new ConsoleFragment();
        fragment.setArguments(args);
        fragment.setContext(context);
        fragment.setTitle("TERMINAL");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currienttext=new StringBuilder();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_console, container, false);
        editText=view.findViewById(R.id.input_command_edit_text);
        commandLog=view.findViewById(R.id.console_log_text_view);
        sendButton=view.findViewById(R.id.send_button);
        sendButton.setOnClickListener(onClickListener);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activity=(ServerInfoActivity)getActivity();
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
