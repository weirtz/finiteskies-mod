package finiteskies.fsmod.blocks.plants;

import java.util.List;
import java.util.Random;
import javax.annotation.Nullable;

import finiteskies.fsmod.FSMod;
import finiteskies.fsmod.Reference;
import finiteskies.fsmod.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StrawberryPlant extends BlockBush implements IGrowable, net.minecraftforge.common.IShearable
{
    public static final PropertyEnum<StrawberryPlant.EnumType> TYPE = PropertyEnum.<StrawberryPlant.EnumType>create("type", StrawberryPlant.EnumType.class);
    protected static final AxisAlignedBB TALL_GRASS_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

    public StrawberryPlant()
    {
        super(Material.VINE);
        setUnlocalizedName(Reference.FiniteBlocks.STRAWBERRYPLANT.getUnlocalizedName());
		setRegistryName(Reference.FiniteBlocks.STRAWBERRYPLANT.getRegistryName());
		this.setSoundType(SoundType.PLANT);
		setCreativeTab(FSMod.CREATIVE_TAB_PLANTS);
    }

    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return TALL_GRASS_AABB;
    }

    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
    {
        return super.canBlockStay(worldIn, pos, state);
    }

    /**
     * Whether this Block can be replaced directly by other blocks (true for e.g. tall grass)
     */
    public boolean isReplaceable(IBlockAccess worldIn, BlockPos pos)
    {
        return false;
    }

    /**
     * Get the Item that this Block should drop when harvested.
     */
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return ModItems.strawberry;
    }

    /**
     * Get the quantity dropped based on the given fortune level
     */
    public int quantityDroppedWithBonus(int fortune, Random random)
    {
        return 1 + random.nextInt(fortune * 2 + 1);
    }

    @SuppressWarnings("unused")
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, @Nullable ItemStack stack)
    {
        if (false && !worldIn.isRemote && stack != null && stack.getItem() == Items.SHEARS) // Forge: Noop Taken care of by IShearable
        {
            player.addStat(StatList.getBlockStats(this));
            spawnAsEntity(worldIn, pos, new ItemStack(Blocks.TALLGRASS, 1, ((StrawberryPlant.EnumType)state.getValue(TYPE)).getMeta()));
        }
        else
        {
            super.harvestBlock(worldIn, player, pos, state, te, stack);
        }
    }

    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(this, 1, state.getBlock().getMetaFromState(state));
    }

    /**
     * Whether this IGrowable can grow
     */

    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
    {
        return true;
    }

    /**
     * Get the OffsetType for this Block. Determines if the model is rendered slightly offset.
     */
    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType()
    {
        return Block.EnumOffsetType.XYZ;
    }

    public static enum EnumType implements IStringSerializable
    {
        DEAD_BUSH(0, "dead_bush"),
        GRASS(1, "tall_grass"),
        FERN(2, "fern");

        private static final StrawberryPlant.EnumType[] META_LOOKUP = new StrawberryPlant.EnumType[values().length];
        private final int meta;
        private final String name;

        private EnumType(int meta, String name)
        {
            this.meta = meta;
            this.name = name;
        }

        public int getMeta()
        {
            return this.meta;
        }

        public String toString()
        {
            return this.name;
        }

        public static StrawberryPlant.EnumType byMetadata(int meta)
        {
            if (meta < 0 || meta >= META_LOOKUP.length)
            {
                meta = 0;
            }

            return META_LOOKUP[meta];
        }

        public String getName()
        {
            return this.name;
        }

        static
        {
            for (StrawberryPlant.EnumType blocktallgrass$enumtype : values())
            {
                META_LOOKUP[blocktallgrass$enumtype.getMeta()] = blocktallgrass$enumtype;
            }
        }
    }

    @Override public boolean isShearable(ItemStack item, IBlockAccess world, BlockPos pos){ return true; }
    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
    {
        List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
        ret.add(new ItemStack(Blocks.TALLGRASS, 1, ((StrawberryPlant.EnumType)world.getBlockState(pos).getValue(TYPE)).getMeta()));
        return ret;
    }
    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        List<ItemStack> ret = new java.util.ArrayList<ItemStack>();
        if (RANDOM.nextInt(8) != 0) return ret;
        ItemStack seed = net.minecraftforge.common.ForgeHooks.getGrassSeed(RANDOM, fortune);
        if (seed != null) ret.add(seed);
        return ret;
    }

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		// TODO Auto-generated method stub
		
	}


}