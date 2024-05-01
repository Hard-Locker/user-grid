package com.halot.nikitazolin.usergrid;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Profile("development")
@RequiredArgsConstructor
public class ApplicationRunnerImpl implements ApplicationRunner {

  @Override
  public void run(ApplicationArguments args) throws Exception {
    

    System.out.println("Ready!");
    log.info("Ready!");
  }
}
