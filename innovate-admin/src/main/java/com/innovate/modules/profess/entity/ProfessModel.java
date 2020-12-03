package com.innovate.modules.profess.entity;

import lombok.Data;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @Author 冯天华
 * @Date 2020/12/2 21:36
 * @Version 1.0
 */
@Data
public class ProfessModel {
    InnovateProfessAchieveEntity professAchieveEntity;
    List<InnovateProfessAttachEntity> attachEntityList;
    List<InnovateProfessAttachEntity> delAttachEntityList;
}
