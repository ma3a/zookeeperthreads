### Project that reproduce thread growing on ZK property changed 
This project indicates a problem desribed in [issue](https://github.com/spring-cloud/spring-cloud-zookeeper/issues/123).

Test that shows that treads are grow: `ZookeeperthreadsApplicationTests.java`
 
##### Issue in common:
1. Application configured to read all properties from ZK path `/root/app`
1. `ZookeeperAutoConfiguration` creates `CuratorFramework`, after that `TreeCache` objects created to listen all changes.
    On this step we we've got 4 Curators threads. 
1. Changing property value on `/root/app` with some data
1. `ContextRefresher` called.
1.  `ZookeeperAutoConfiguration` invoked, and these leads to creation a new `CuratorFramework` object. At this point 
    we've got 8 Curator threads 


#### Notes
Issue was found with real connection to Zookeeper server, for testing purpose `TetingServer` used. 