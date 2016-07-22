package com.campusconnect.cc_reboot.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.campusconnect.cc_reboot.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by rkd on 15/01/16.
 */
public class FAQListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //FAQ variables start region
    String[] questions = {
            "Who contributes the notes?",
            "How are notes screened?",
            "What kind of notes are allowed/not-allowed on Campus Connect?",
            "Can I upload handwritten notes?",
            "What should I do if my class uses Powerpoint Slides?",
            "How do I upload content?",
            "Why am I not able to upload into a course?",
            "Can I submit notes for multiple courses?",
            "How to Report Content?",
            "How to Report Course?",
            "What are the file formats supported by Campus Connect?",
            "How to delete a Campus Connect account?"
    };

    String[] answers = {
            "All the notes you see and find on Campus Connect are created by fellow students who have taken or may be currently taking the same courses as you. They share their valuable course notes in order to help other students. ",
            "We use an automated system to evaluate each set of notes that is uploaded to the Campus Connect platform. Our loyal community of users have the ability to report documents they see as unrelated or irrelevant. As well, we have an internal audit team that checks over all documents on a daily basis.",
            "We only accept uploaded documents that are 100% created by students. The following types of documents are accepted on CampusConnect.com: lecture notes, textbook notes, chapter summaries, study guides.\nWe do not accept the following types of documents: (1) copyright documents created by professors or instructors, including but not limited to: powerpoint slides, assignment questions, past exams, textbooks, e-textbooks, published articles, journals; (2) homework, labs, assignments, essays; (3) duplicate documents; (4) single page documents with little or no information; (5) one full set of notes separated into multiple documents in order to gain additional credits.",
            "Of course, we have an advanced technology that would enhance the quality of your notes and produce scan-like quality images instantly.",
            "Currently we do not support .ppt formats, however you can export the slides as images and upload it as a batch.",
            "You can upload content only if you have subscribed to that particular course. You can upload content by clicking upload button and choosing the type of the content.",
            "You can not upload into a course for which you are not subscribed. You can add new course or click on the search bar and search for the courses that you are interested in.",
            "Absolutely! there is no limit on the number of courses you can submit notes for. We encourage contributions to the community.",
            "You can report a content if you find the content is inappropriate, false information on content or even a copyrighted content by just clicking the report button.",
            "You can report a course if you find the content of the course is inappropriate or false information by just clicking the report button.",
            "Currently we support various image formats such as .png, .jpg, .jpeg, etc.,",
            "To request closure of your Campus Connect account, just send us an email to info@campusconnect.com and we will be able to assist you with your request right away."
    };

    //end region
    Context context;

    // region Constructors
    public FAQListAdapter(Context context) {
        this.context = context;
    }
    // endregion

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.card_layout_faq, parent, false);

        return new FAQListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final FAQListViewHolder faq_viewholder = (FAQListViewHolder) viewHolder;

            faq_viewholder.question.setText(questions[position]);
            faq_viewholder.answer.setText(answers[position]);

    }


    @Override
    public int getItemCount() {
        return questions.length;
    }

    public class FAQListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tv_question)
        TextView question;
        @Bind(R.id.tv_answer)
        TextView answer;

        public FAQListViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);

        }

    }
}
