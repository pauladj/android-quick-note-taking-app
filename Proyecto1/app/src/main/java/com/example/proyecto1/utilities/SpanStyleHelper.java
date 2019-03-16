package com.example.proyecto1.utilities;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.EditText;

/***
 * Basado en el código extraído de Stack Overflow
 * Pregunta: https://stackoverflow.com/questions/36290847/edit-spanned-text/
 * Autor: https://stackoverflow.com/users/1621977/mostafa-lavaei
 * Modificado por Paula de Jaime para evitar que si un texto anterior al seleccionado
 * no tiene ningún estilo esto siga así.
 **/
public class SpanStyleHelper {
    protected EditText mEditText;
    protected Spannable mSpannable;
    protected int mSelectedTextStart;
    protected int mSelectedTextEnd;

    public SpanStyleHelper(EditText editText) {
        mEditText = editText;
        mSpannable = mEditText.getText();
        mSelectedTextStart = mEditText.getSelectionStart();
        mSelectedTextEnd = mEditText.getSelectionEnd();
    }

    public Spannable boldSelectedText() {
        Log.d("Ramansoft", "Try to bold selected text..");

        StyleSpan[] styleSpans = mEditText.getText().getSpans(
                mSelectedTextStart,
                mSelectedTextEnd,
                StyleSpan.class
        );

        if (styleSpans.length > 0) {
            int lastSpanEnd = 0;

            for (StyleSpan styleSpan : styleSpans) {
                /**
                 * Save old style
                 */
                int oldStyle = styleSpan.getStyle();

                /**
                 * Get start and end of span
                 */
                int spanStart = mSpannable.getSpanStart(styleSpan);
                int spanEnd = mSpannable.getSpanEnd(styleSpan);


                /**
                 * Update last span end
                 */
                lastSpanEnd = spanEnd;

                /**
                 * Remove the span
                 */
                mSpannable.removeSpan(styleSpan);

                /**
                 * Because we just need change selected text,
                 * if span start is lower than selected text start or
                 * if span end is higher than selected text end start
                 * we should restore span for unselected part of span
                 */
                if (spanStart < mEditText.getSelectionStart()) {
                    mSpannable.setSpan(
                            new StyleSpan(oldStyle),
                            spanStart,
                            mSelectedTextStart,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }

                if (spanEnd > mEditText.getSelectionEnd()) {
                    mSpannable.setSpan(
                            new StyleSpan(oldStyle),
                            mSelectedTextEnd,
                            spanEnd,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }

                /**
                 * We want to add bold style to current style
                 * so we most detect current style and change
                 * the style depend on current style
                 */
                if (oldStyle == Typeface.ITALIC) {
                    mSpannable.setSpan(
                            new StyleSpan(Typeface.BOLD_ITALIC),
                            spanStart,
                            spanEnd,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                } else {
                    mSpannable.setSpan(
                            new StyleSpan(Typeface.BOLD),
                            spanStart,
                            spanEnd,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }
            }

            /**
             * Now we should check if any
             * unspanned selected text remains
             */
            if (mSelectedTextEnd != lastSpanEnd) {
                mSpannable.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        lastSpanEnd,
                        mSelectedTextEnd,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
        } else {
            mSpannable.setSpan(
                    new StyleSpan(Typeface.BOLD),
                    mSelectedTextStart,
                    mSelectedTextEnd,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        return mSpannable;
    }

    public Spannable unBoldSelectedText() {
        Log.d("Ramansoft", "Try to unbold selected text..");

        StyleSpan[] styleSpans = mEditText.getText().getSpans(
                mSelectedTextStart,
                mSelectedTextEnd,
                StyleSpan.class
        );

        for (StyleSpan styleSpan : styleSpans) {
            /**
             * Save old style
             */
            int oldStyle = styleSpan.getStyle();


            /**
             * Get start and end of span
             */
            int spanStart = mSpannable.getSpanStart(styleSpan);
            int spanEnd = mSpannable.getSpanEnd(styleSpan);


            /**
             * Remove the span
             */
            mSpannable.removeSpan(styleSpan);


            /**
             * Because we just need change selected text,
             * if span start is lower than selected text start or
             * if span end is higher than selected text end start
             * we should restore span for unselected part of span
             */
            if (spanStart < mEditText.getSelectionStart()) {
                mSpannable.setSpan(
                        new StyleSpan(oldStyle),
                        spanStart,
                        mSelectedTextStart,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }

            if (spanEnd > mEditText.getSelectionEnd()) {
                mSpannable.setSpan(
                        new StyleSpan(oldStyle),
                        mSelectedTextEnd,
                        spanEnd,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }

            /**
             * Because we just want to remove bold style,
             * if the span has another style, we should restore it
             */
            if (oldStyle == Typeface.BOLD_ITALIC) {
                mSpannable.setSpan(
                        new StyleSpan(Typeface.ITALIC),
                        spanStart,
                        spanEnd,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
        }

        return mSpannable;
    }

    public Spannable toggleBoldSelectedText() {
        Log.d("Ramansoft", "Try to toggle bold selected text..");

        boolean isAllSpansBold = true;

        StyleSpan[] styleSpans = mEditText.getText().getSpans(
                mSelectedTextStart,
                mSelectedTextEnd,
                StyleSpan.class
        );

        if (styleSpans.length == 0) {
            return boldSelectedText();
        } else {
            for (StyleSpan styleSpan : styleSpans) {
                Log.d("Ramansoft", "styleSpan.getStyle() = " + styleSpan.getStyle());

                if (styleSpan.getStyle() != Typeface.BOLD && styleSpan.getStyle() != Typeface.BOLD_ITALIC) {
                    isAllSpansBold = false;
                    break;
                }
            }

            Log.d("Ramansoft", "isAllSpansBold = " + isAllSpansBold);

            if (isAllSpansBold)
                return unBoldSelectedText();
            else
                return boldSelectedText();
        }

    }

    public Spannable italicSelectedText() {
        Log.d("Ramansoft", "Try to italic selected text..");

        StyleSpan[] styleSpans = mEditText.getText().getSpans(
                mSelectedTextStart,
                mSelectedTextEnd,
                StyleSpan.class
        );

        if (styleSpans.length > 0) {
            int lastSpanEnd = 0;

            for (StyleSpan styleSpan : styleSpans) {
                /**
                 * Save old style
                 */
                int oldStyle = styleSpan.getStyle();


                /**
                 * Get start and end of span
                 */
                int spanStart = mSpannable.getSpanStart(styleSpan);
                int spanEnd = mSpannable.getSpanEnd(styleSpan);

                /**
                 * Update last span end
                 */
                lastSpanEnd = spanEnd;


                /**
                 * Remove the span
                 */
                mSpannable.removeSpan(styleSpan);


                /**
                 * Because we just need change selected text,
                 * if span start is lower than selected text start or
                 * if span end is higher than selected text end start
                 * we should restore span for unselected part of span
                 */
                if (spanStart < mEditText.getSelectionStart()) {
                    mSpannable.setSpan(
                            new StyleSpan(oldStyle),
                            spanStart,
                            mSelectedTextStart,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }

                if (spanEnd > mEditText.getSelectionEnd()) {
                    mSpannable.setSpan(
                            new StyleSpan(oldStyle),
                            mSelectedTextEnd,
                            spanEnd,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }

                /**
                 * We want to add bold style to current style
                 * so we most detect current style and change
                 * the style depend on current style
                 */
                if (oldStyle == Typeface.BOLD) {
                    mSpannable.setSpan(
                            new StyleSpan(Typeface.BOLD_ITALIC),
                            spanStart,
                            spanEnd,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                } else {
                    mSpannable.setSpan(
                            new StyleSpan(Typeface.ITALIC),
                            spanStart,
                            spanEnd,
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                    );
                }
            }

            /**
             * Now we should check if any
             * unspanned selected text remains
             */
            if (mSelectedTextEnd != lastSpanEnd) {
                mSpannable.setSpan(
                        new StyleSpan(Typeface.ITALIC),
                        lastSpanEnd,
                        mSelectedTextEnd,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
        } else {
            mSpannable.setSpan(
                    new StyleSpan(Typeface.ITALIC),
                    mSelectedTextStart,
                    mSelectedTextEnd,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
        }

        return mSpannable;
    }

    public Spannable unItalicSelectedText() {
        Log.d("Ramansoft", "Try to un-italic selected text..");

        StyleSpan[] styleSpans = mEditText.getText().getSpans(
                mSelectedTextStart,
                mSelectedTextEnd,
                StyleSpan.class
        );

        for (StyleSpan styleSpan : styleSpans) {
            /**
             * Save old style
             */
            int oldStyle = styleSpan.getStyle();

            /**
             * Get start and end of span
             */
            int spanStart = mSpannable.getSpanStart(styleSpan);
            int spanEnd = mSpannable.getSpanEnd(styleSpan);

            /**
             * Remove the span
             */
            mSpannable.removeSpan(styleSpan);


            /**
             * Because we just need change selected text,
             * if span start is lower than selected text start or
             * if span end is higher than selected text end start
             * we should restore span for unselected part of span
             */
            if (spanStart < mEditText.getSelectionStart()) {
                mSpannable.setSpan(
                        new StyleSpan(oldStyle),
                        spanStart,
                        mSelectedTextStart,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }

            if (spanEnd > mEditText.getSelectionEnd()) {
                mSpannable.setSpan(
                        new StyleSpan(oldStyle),
                        mSelectedTextEnd,
                        spanEnd,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }

            /**
             * Because we just want to remove bold style,
             * if the span has another style, we should restore it
             */
            if (oldStyle == Typeface.BOLD_ITALIC) {
                mSpannable.setSpan(
                        new StyleSpan(Typeface.BOLD),
                        spanStart,
                        spanEnd,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );
            }
        }

        return mSpannable;
    }

    public Spannable toggleItalicSelectedText() {
        Log.d("Ramansoft", "Try to toggle italic selected text..");

        boolean isAllSpansItalic = true;


        StyleSpan[] styleSpans = mEditText.getText().getSpans(
                mSelectedTextStart,
                mSelectedTextEnd,
                StyleSpan.class
        );


        if (styleSpans.length == 0) {
            return italicSelectedText();
        } else {
            for (StyleSpan styleSpan : styleSpans) {
                Log.d("Ramansoft", "styleSpan.getStyle() = " + styleSpan.getStyle());

                if (styleSpan.getStyle() != Typeface.ITALIC && styleSpan.getStyle() != Typeface.BOLD_ITALIC) {
                    isAllSpansItalic = false;
                    break;
                }
            }

            Log.d("Ramansoft", "isAllSpansItalic = " + isAllSpansItalic);


            if (isAllSpansItalic)
                return unItalicSelectedText();
            else
                return italicSelectedText();
        }
    }
}