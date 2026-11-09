package com.project.sethu.inventory_store.service;

import com.project.sethu.inventory_store.repository.ItemRepository;
import com.project.sethu.inventory_store.repository.OrganisationRepository;
import com.project.sethu.inventory_store.repository.UserOrganisationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class InactiveRecordCleanupService {

    private static final Logger log = LoggerFactory.getLogger(InactiveRecordCleanupService.class);

    private final ItemRepository itemRepository;
    private final OrganisationRepository organisationRepository;
    private final UserOrganisationRepository userOrganisationRepository;

    public InactiveRecordCleanupService(
            ItemRepository itemRepository,
            OrganisationRepository organisationRepository,
            UserOrganisationRepository userOrganisationRepository) {
        this.itemRepository = itemRepository;
        this.organisationRepository = organisationRepository;
        this.userOrganisationRepository = userOrganisationRepository;
    }

    @Scheduled(cron = "${inventory.cleanup.cron:0 0 0 * * ?}")
    @Transactional
    public void cleanupExpiredInactiveRecords() {
        OffsetDateTime cutoff = OffsetDateTime.now().minusMonths(6);

        List<UUID> expiredOrganisationIds = organisationRepository.findExpiredInactiveOrganisationIds(cutoff);
        if (!expiredOrganisationIds.isEmpty()) {
            itemRepository.deleteByOrganisationIds(expiredOrganisationIds);
            userOrganisationRepository.deleteByOrganisationIds(expiredOrganisationIds);
            int deletedOrganisations = organisationRepository.deleteExpiredInactiveOrganisations(cutoff);
            log.info("Deleted {} expired inactive organisations", deletedOrganisations);
        }

        int deletedItems = itemRepository.deleteExpiredInactiveItems(cutoff);
        log.info("Deleted {} expired inactive items", deletedItems);
    }
}
