package backend.FinSight.service;

import backend.FinSight.dto.ChatResponse;

import org.springframework.stereotype.Service;

@Service
public class AIChatService {

    public ChatResponse getReply(
            String message
    ) {

        String lower =
                message.toLowerCase();

        String reply;

        // SAVINGS

        if (lower.contains("save")) {

            reply =
                    "Reduce Food and Shopping expenses to improve savings.";

        }

        // FOOD

        else if (lower.contains("food")) {

            reply =
                    "Your food expenses are increasing. Try reducing online orders.";
        }

        // BUDGET

        else if (lower.contains("budget")) {

            reply =
                    "Set category-wise spending limits for better financial health.";
        }

        // INVESTMENT

        else if (lower.contains("invest")) {

            reply =
                    "You can allocate a fixed percentage of monthly income to investments.";
        }

        // GENERAL

        else {

            reply =
                    "Track your expenses regularly to improve financial stability.";
        }

        return new ChatResponse(reply);
    }
}
