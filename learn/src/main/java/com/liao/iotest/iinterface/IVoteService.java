package com.liao.iotest.iinterface;

import com.liao.iotest.demo.vo.MonitorCandidate;

public interface IVoteService {
    public boolean inc(int sid);
    public MonitorCandidate[] result();
    public MonitorCandidate[] getData();
}
