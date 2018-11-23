// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.
package io.vlingo.reactive.messaging.patterns.scattergather;

import org.junit.Test;

import io.vlingo.actors.Definition;
import io.vlingo.actors.World;
import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.reactive.messaging.patterns.scattergather.MountainSuppliesOrderProcessor.BudgetHikersPriceQuotes;
import io.vlingo.reactive.messaging.patterns.scattergather.MountainSuppliesOrderProcessor.HighSierraPriceQuotes;
import io.vlingo.reactive.messaging.patterns.scattergather.MountainSuppliesOrderProcessor.MountainAscentPriceQuotes;
import io.vlingo.reactive.messaging.patterns.scattergather.MountainSuppliesOrderProcessor.PinnacleGearPriceQuotes;
import io.vlingo.reactive.messaging.patterns.scattergather.MountainSuppliesOrderProcessor.PriceQuoteAggregator;
import io.vlingo.reactive.messaging.patterns.scattergather.MountainSuppliesOrderProcessor.RockBottomOuterwearPriceQuotes;
import io.vlingo.reactive.messaging.patterns.scattergather.RetailBasket.RetailItem;

/**
 * ScatterGatherTest driver for this scatter-gather example.
 *
 * @author brsg.io
 * @since Nov 20, 2018
 */
public class ScatterGatherTest
{
    public static final String WORLD_NAME = "scatter-gather-example";

    @Test
    public void testScatterGatherRuns()
    {
        World world = World.startWithDefaults( WORLD_NAME );
        world.defaultLogger().log( "ScatterGatherTest: is started" );
        
        TestUntil untilRegistered = TestUntil.happenings( 5 );
        TestUntil until = TestUntil.happenings( 5 );
        
        AggregateProcessor priceQuoteAggregator = world.actorFor( Definition.has( PriceQuoteAggregator.class, Definition.NoParameters ), AggregateProcessor.class );
        OrderProcessor mtnSppliesOrderProcessor = world.actorFor( Definition.has( MountainSuppliesOrderProcessor.class, Definition.parameters( priceQuoteAggregator, until, untilRegistered )), OrderProcessor.class );
        world.actorFor( Definition.has( BudgetHikersPriceQuotes.class, Definition.parameters( mtnSppliesOrderProcessor )), QuoteProcessor.class );
        world.actorFor( Definition.has( HighSierraPriceQuotes.class, Definition.parameters( mtnSppliesOrderProcessor )), QuoteProcessor.class );
        world.actorFor( Definition.has( MountainAscentPriceQuotes.class, Definition.parameters( mtnSppliesOrderProcessor )), QuoteProcessor.class );
        world.actorFor( Definition.has( PinnacleGearPriceQuotes.class, Definition.parameters( mtnSppliesOrderProcessor )), QuoteProcessor.class );
        world.actorFor( Definition.has( RockBottomOuterwearPriceQuotes.class, Definition.parameters( mtnSppliesOrderProcessor )), QuoteProcessor.class );
        
        untilRegistered.completes();
        world.defaultLogger().log( String.format( "Register completes!!!" ));
        
        mtnSppliesOrderProcessor
            .requestForQuote( 
                new RetailBasket( 
                    "123", 
                    new RetailItem( "1", 29.95 ), 
                    new RetailItem( "2", 99.95 ), 
                    new RetailItem( "3", 14.95 )
                )
            );
        
        mtnSppliesOrderProcessor
            .requestForQuote( 
                new RetailBasket( 
                    "125", 
                    new RetailItem( "4", 39.95 ), 
                    new RetailItem( "5", 199.95 ), 
                    new RetailItem( "6", 149.95 ),
                    new RetailItem( "7", 724.99 )
                )
            );
    
        mtnSppliesOrderProcessor
            .requestForQuote( 
                new RetailBasket( 
                    "129", 
                    new RetailItem( "8", 119.99 ), 
                    new RetailItem( "9", 499.95 ), 
                    new RetailItem( "10", 519.00 ),
                    new RetailItem( "11", 209.50 )
                )
            );
    
        mtnSppliesOrderProcessor
            .requestForQuote( 
                new RetailBasket( 
                    "135", 
                    new RetailItem( "12", .97 ), 
                    new RetailItem( "13", 9.50 ), 
                    new RetailItem( "14", 1.99 )
                )
            );
    
        mtnSppliesOrderProcessor
            .requestForQuote( 
                new RetailBasket( 
                    "140", 
                    new RetailItem( "15", 107.50 ), 
                    new RetailItem( "16", 9.50 ), 
                    new RetailItem( "17", 599.99 ),
                    new RetailItem( "18", 249.95 ),
                    new RetailItem( "19", 789.99 )
                )
            );
    
        until.completes();
        
        world.defaultLogger().log( "ScatterGatherTest: is completed" );
        world.terminate();
    }

}