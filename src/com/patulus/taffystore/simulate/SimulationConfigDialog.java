package com.patulus.taffystore.simulate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SimulationConfigDialog extends JDialog {
    /** 시뮬레이션 할 일 수 (일) */
    private JTextField daysField;

    /** 점원의 수 */
    private JTextField nServerField;
    /** 점원의 하루 근로 시간 (분) */
    private JTextField workingTimeField;
    /** 최소 서비스 시간 */
    private JTextField minSvcTimeField;
    /** 최대 서비스 시간 */
    private JTextField maxSvcTimeField;
    /** 최소 전화 서비스 시간 */
    private JTextField minCallSvcTimeField;
    /** 최대 전화 서비스 시간 */
    private JTextField maxCallSvcTimeField;

    /** 손님의 평균 방문 간격 */
    private JTextField avgArrivalCycleField;
    /** VIP 등장 확률 */
    private JTextField probVipField;
    /** 전화가 올 확률 */
    private JTextField probCallField;

    private SimulationConfig simulationConfig;

    public SimulationConfigDialog(Frame frame) {
        super(frame, "시뮬레이션 설정", true);
        setLayout(new GridLayout(12, 2));

        // 라벨, 입력 필드 추가
        add(new JLabel("시뮬레이션 일 수: "));
        daysField = new JTextField("5"); // 기본 값은 5일로 지정
        add(daysField);

        add(new JLabel("점원 수: "));
        nServerField = new JTextField("2"); // 기본 값은 2명으로 지정
        add(nServerField);

        add(new JLabel("하루 근무 시간 (분): "));
        workingTimeField = new JTextField("480"); // 기본 값은 480분으로 지정
        add(workingTimeField);

        add(new JLabel("평균 고객 도착 간격 (분): "));
        avgArrivalCycleField = new JTextField("4"); // 기본 값은 4분으로 지정
        add(avgArrivalCycleField);

        add(new JLabel("최소 서비스 시간: "));
        minSvcTimeField = new JTextField("1"); // 기본 값은 1분으로 지정
        add(minSvcTimeField);

        add(new JLabel("최대 서비스 시간:"));
        maxSvcTimeField = new JTextField("10"); // 기본 값은 10분으로 지정
        add(maxSvcTimeField);

        add(new JLabel("최소 전화 서비스 시간:"));
        minCallSvcTimeField = new JTextField("1"); // 기본 값은 1분로 지정
        add(minCallSvcTimeField);

        add(new JLabel("최대 전화 서비스 시간:"));
        maxCallSvcTimeField = new JTextField("3"); // 기본 값은 3분으로 지정
        add(maxCallSvcTimeField);

        add(new JLabel("VIP 등장 확률 (0~1):"));
        probVipField = new JTextField("0.01"); // 기본 값은 1%로 지정
        add(probVipField);

        add(new JLabel("전화 등장 확률 (0~1):"));
        probCallField = new JTextField("0.05"); // 기본 값은 5%로 지정
        add(probCallField);

        // 확인, 취소 버튼 추가
        JButton btnOk = new JButton("확인");
        JButton btnCancel = new JButton("취소");
        add(btnOk);
        add(btnCancel);

        btnOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int days = Integer.parseInt(daysField.getText());
                    int nServer = Integer.parseInt(nServerField.getText());
                    int workingTime = Integer.parseInt(workingTimeField.getText());
                    int avgArrivalCycle = Integer.parseInt(avgArrivalCycleField.getText());
                    int minSvcTime = Integer.parseInt(minSvcTimeField.getText());
                    int maxSvcTime = Integer.parseInt(maxSvcTimeField.getText());
                    int minCallSvcTime = Integer.parseInt(minCallSvcTimeField.getText());
                    int maxCallSvcTime = Integer.parseInt(maxCallSvcTimeField.getText());
                    double probVip = Double.parseDouble(probVipField.getText());
                    double probCall = Double.parseDouble(probCallField.getText());

                    simulationConfig = new SimulationConfig(days, nServer, workingTime, avgArrivalCycle,
                            minSvcTime, maxSvcTime, minCallSvcTime, maxCallSvcTime, probVip, probCall);

                    // 프레임 숨기기
                    dispose();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(SimulationConfigDialog.this,
                            "입력 값이 올바르지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 취소 버튼을 누르면 프로그램을 종료
                simulationConfig = null;
                dispose();
                System.exit(0);
            }
        });

        // 모든 내용이 표시되도록 프레임 크기 조절
        pack();
        // frame을 기준으로 가운데 정렬
        setLocationRelativeTo(frame);
    }

    public SimulationConfig getSimulationConfig() {
        return simulationConfig;
    }
}
