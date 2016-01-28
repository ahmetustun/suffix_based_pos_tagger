package core;

/**
 * Created by ahmet on 22/01/16.
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;

public class Viterbi
{

   public static ArrayList<String> forwardViterbi(String[] obs, String[] states,
                                                  HashMap<String, Float> start_p,
                                                  HashMap<String, HashMap<String, Float>> trans_p,
                                                  HashMap<String, HashMap<String, Float>> emit_p) {
        ArrayList<String> statesList = new ArrayList<>();
        Hashtable<String, Object[]> T = new Hashtable<String, Object[]>();
        for (String state : states)
            T.put(state, new Object[] {start_p.get(state), state, start_p.get(state)});

        for (String output : obs)
        {
            Hashtable<String, Object[]> U = new Hashtable<String, Object[]>();
            for (String next_state : states)
            {
                float total = 0;
                String argmax = "";
                float valmax = 0;

                float prob = 1;
                String v_path = "";
                float v_prob = 1;

                for (String source_state : states)
                {
                    Object[] objs = T.get(source_state);
                    prob = ((Float) objs[0]).floatValue();
                    v_path = (String) objs[1];
                    v_prob = ((Float) objs[2]).floatValue();

                    float p = emit_p.get(source_state).get(output) *
                            trans_p.get(source_state).get(next_state);
                    prob *= p;
                    v_prob *= p;
                    total += prob;
                    if (v_prob > valmax)
                    {
                        argmax = v_path + "," + next_state;
                        valmax = v_prob;
                    }
                }
                U.put(next_state, new Object[] {total, argmax, valmax});
            }
            T = U;
        }

        float total = 0;
        String argmax = "";
        float valmax = 0;

        float prob;
        String v_path;
        float v_prob;

        for (String state : states)
        {
            Object[] objs = T.get(state);
            prob = ((Float) objs[0]).floatValue();
            v_path = (String) objs[1];
            v_prob = ((Float) objs[2]).floatValue();
            total += prob;
            if (v_prob > valmax)
            {
                argmax = v_path;
                valmax = v_prob;
            }
        }

        String[] sList = argmax.split(",");
        for (int i=0; i<sList.length-1; i++){
            statesList.add(sList[i]);
        }
        return statesList;
    }
}