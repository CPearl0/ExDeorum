/*
 * Ex Deorum
 * Copyright (c) 2023 thedarkcolour
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package thedarkcolour.exdeorum.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.DoubleValue;
import org.apache.commons.lang3.tuple.Pair;

public class EConfig {
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final ForgeConfigSpec SERVER_SPEC;
    public static final Client CLIENT;
    public static final Server SERVER;

    public static class Client {
        public final BooleanValue useFastInfestedLeaves;
        public final BooleanValue setVoidWorldAsDefault;

        public Client(ForgeConfigSpec.Builder builder) {
            builder.comment("Client configuration for Ex Deorum").push("client");

            this.useFastInfestedLeaves = builder
                    .comment("Whether to use a simplified renderer for infested leaves (reduces FPS lag with lots of infested trees)")
                    .define("use_fast_infested_leaves", false);
            this.setVoidWorldAsDefault = builder
                    .comment("Whether the Void World type is set as the default world preset in the world creation screen.")
                    .define("set_void_world_as_default", true);

            builder.pop();
        }
    }

    public static class Server {
        public final BooleanValue startingTorch;
        public final BooleanValue startingWateringCan;
        public final BooleanValue simultaneousSieveUsage;
        public final DoubleValue barrelProgressStep;
        public final BooleanValue witchWaterNetherrackGenerator;
        public final BooleanValue setVoidWorldAsDefault;

        // todo preferred ores
        //public final ConfigValue<ResourceLocation> preferredAluminumOre;
        //public final ConfigValue<ResourceLocation> preferredSilverOre;
        //public final ConfigValue<ResourceLocation> preferredLeadOre;
        //public final ConfigValue<ResourceLocation> preferredNickelOre;
        //public final ConfigValue<ResourceLocation> preferredUraniumOre;
        //public final ConfigValue<ResourceLocation> preferredOsmiumOre;
        //public final ConfigValue<ResourceLocation> preferredTinOre;
        //public final ConfigValue<ResourceLocation> preferredZincOre;
        //public final ConfigValue<ResourceLocation> preferredCobaltOre;
        //public final ConfigValue<ResourceLocation> preferredIridiumOre;


        public Server(ForgeConfigSpec.Builder builder) {
            builder.comment("Server configuration for Ex Deorum").push("server");

            this.startingTorch = builder
                    .comment("Whether players in a void world start out with a torch or not.")
                    .define("starting_torch", true);
            this.startingWateringCan = builder
                    .comment("Whether players in a void world start out with a full wooden watering can.")
                    .define("starting_watering_can", true);
            this.simultaneousSieveUsage = builder
                    .comment("Whether players can use multiple sieves in a 3x3 area at once.")
                    .define("simultaneous_sieve_usage", true);
            this.barrelProgressStep = builder
                    .comment("The progress to increment by each tick for barrel composting and witch water transformation.")
                    .defineInRange("barrel_progress_step", 0.004, 0.0f, 1.0f);
            this.witchWaterNetherrackGenerator = builder
                    .comment("Whether Witch Water forms netherrack when lava flows into it, allowing for a netherrack version of a cobblestone generator.")
                    .define("witch_water_netherrack_generator", true);
            this.setVoidWorldAsDefault = builder
                    .comment("Whether the Void World type is used by default in the \"server.properties\" file when creating a server.")
                    .define("set_void_world_as_default", true);

            // Preferred ore items
            //builder.push("preferred_tag_items");

            //this.preferredTinOre = builder
            //        .comment("The ID of the item to use for Ex Deorum recipes that craft into tin ore.")
            //        .define("preferred_tin_ore", (ResourceLocation) null);

            //builder.pop();
            builder.pop();
        }
    }

    static {
        {
            Pair<Server, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Server::new);
            SERVER = specPair.getLeft();
            SERVER_SPEC = specPair.getRight();
        }
        {
            Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
            CLIENT = specPair.getLeft();
            CLIENT_SPEC = specPair.getRight();
        }
    }
}
