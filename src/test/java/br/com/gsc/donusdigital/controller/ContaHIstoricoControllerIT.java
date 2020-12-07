package br.com.gsc.donusdigital.controller;

import org.springframework.test.context.jdbc.Sql;

@Sql({"/db/axd-user-admin-dataset.sql", "/db/customer-dataset.sql", "/db/axd-user-customer-dataset.sql",
        "/db/push-notification-subscription-dataset.sql"})
@Sql(scripts = {"/db/cleanup.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ContaHIstoricoControllerIT extends AbstractBaseIT {
}
