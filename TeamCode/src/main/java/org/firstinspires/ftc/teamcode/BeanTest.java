package org.firstinspires.ftc.teamcode;

import androidx.annotation.NonNull;

import om.self.beans.Bean;

@Bean(alwaysLoad = true)
public class BeanTest {

    @NonNull
    @Override
    public String toString() {
        return "it works!";
    }
}
