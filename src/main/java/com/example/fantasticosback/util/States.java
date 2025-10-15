package com.example.fantasticosback.util;

import com.example.fantasticosback.model.RequestStates.RequestState;
import com.example.fantasticosback.model.RequestStates.PendingState;
import com.example.fantasticosback.model.RequestStates.ProcessState;
import com.example.fantasticosback.model.RequestStates.AcceptedState;
import com.example.fantasticosback.model.RequestStates.RejectedState;

import java.util.HashMap;
import java.util.Map;

public class States {
    private static Map<String, RequestState> states = new HashMap<>();

    static {
        states.put("Pending", new PendingState());
        states.put("In Process", new ProcessState());
        states.put("Accepted", new AcceptedState());
        states.put("Rejected", new RejectedState());
    }

    public static Map<String, RequestState> getStates() {
        return states;
    }
}
