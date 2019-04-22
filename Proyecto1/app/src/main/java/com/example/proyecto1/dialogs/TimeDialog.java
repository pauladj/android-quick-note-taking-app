package com.example.proyecto1.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.net.sip.SipSession;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.sql.Time;
import java.util.Calendar;

public class TimeDialog extends DialogFragment implements TimePickerDialog.OnTimeSetListener  {

    ListenerTime miListener;

    public interface ListenerTime {
        void timeSelectedForCalendar(int hour, int minute);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        Calendar calendario = Calendar.getInstance();
        int hora = calendario.get(Calendar.HOUR_OF_DAY);
        int minuto = calendario.get(Calendar.MINUTE);
        TimePickerDialog eldialogo = new TimePickerDialog(getActivity(),this, hora,minuto,
                DateFormat.is24HourFormat(getActivity()));
        return eldialogo;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // la hora se ha elegido, devolver datos
        miListener.timeSelectedForCalendar(hourOfDay, minute);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        miListener = (TimeDialog.ListenerTime) activity;
    }
}