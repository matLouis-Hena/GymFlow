package dataAccessPackage.subscriptionDataAccess;

import exceptionPackage.subscription.ReadSubscriptionException;
import modelPackage.Subscription;

import java.util.List;

public interface ISubscriptionDA {

    List<Subscription> getAll() throws ReadSubscriptionException;

    Subscription getById(int id) throws ReadSubscriptionException;
}