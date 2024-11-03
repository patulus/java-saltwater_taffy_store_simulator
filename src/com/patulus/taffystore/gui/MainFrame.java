package com.patulus.taffystore.gui;

import com.patulus.taffystore.simulate.Simulate;
import com.patulus.taffystore.simulate.SimulationConfig;
import com.patulus.taffystore.simulate.SimulationConfigDialog;
import com.patulus.taffystore.simulate.SimulationPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private SimulationPanel simulationPanel;

    private SimulationConfig config;
    private ProgressDialog progressDialog;
    private Simulate simulate;

    public MainFrame() {
        setTitle("태피 상점 큐 시뮬레이션 결과 요약");
        // 초기 사이즈는 1000 x 600으로 설정
        setSize(1000, 600);
        // 윈도우 프레임 종료 버튼을 누르면 프로그램이 종료되도록 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        setVisible(true);
        setLocationRelativeTo(null);

        // 시뮬레이션 설정 창 표시
        this.config = showConfigDialog();
        if (this.config == null) {
            System.exit(0);
        }

        // 시뮬레이션 실행 중 창 표시
        this.progressDialog = new ProgressDialog(this, "시뮬레이션 실행 중...");

        // 시뮬레이션 실행
        runSimulation(this.config, this.progressDialog);
    }

    private SimulationConfig showConfigDialog() {
        SimulationConfigDialog dialog = new SimulationConfigDialog(this);
        dialog.setVisible(true);
        return dialog.getSimulationConfig();
    }

    private void runSimulation(SimulationConfig config, ProgressDialog progressDialog) {
        Thread simulationThread = new Thread(new SimulationThread());

        // 시뮬레이션 스레드 시작
        simulationThread.start();

        // 진행 중 다이얼로그 표시
        progressDialog.setVisible(true);
    }

    private class SimulationThread implements Runnable {
        @Override
        public void run() {
            simulate = new Simulate(
                    // 설정 정보 넘겨주기
                    config.getDays(),
                    config.getNServer(),
                    config.getWorkingTime(),
                    config.getAvgArrivalCycle(),
                    config.getMinSvcTime(),
                    config.getMaxSvcTime(),
                    config.getMinCallSvcTime(),
                    config.getMaxCallSvcTime(),
                    config.getProbVip(),
                    config.getProbCall()
            );

            // 시뮬레이션 실행
            simulate.run();

            // 진행 중 다이얼로그 닫기
            progressDialog.dispose();

            // 시뮬레이션 패널 생성 및 추가
            simulationPanel = new SimulationPanel(simulate);
            add(simulationPanel, BorderLayout.CENTER);

            revalidate();
            repaint();

            setLocationRelativeTo(null);
        }
    }
}
