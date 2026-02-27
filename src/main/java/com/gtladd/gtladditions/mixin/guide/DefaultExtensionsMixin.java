package com.gtladd.gtladditions.mixin.guide;

import com.gtladd.gtladditions.api.guide.FluidLinkCompiler;
import com.gtladd.gtladditions.api.guide.LatexCompiler;
import guideme.compiler.TagCompiler;
import guideme.compiler.tags.*;
import guideme.internal.extensions.DefaultExtensions;
import guideme.scene.BlockImageTagCompiler;
import guideme.scene.ItemImageTagCompiler;
import guideme.scene.SceneTagCompiler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DefaultExtensions.class)
public class DefaultExtensionsMixin {

    @Inject(method = "tagCompilers",
            at = @At(value = "HEAD",
                     target = "Ljava/util/List;of([Ljava/lang/Object;)Ljava/util/List;"),
            remap = false,
            cancellable = true)
    private static void tagCompilers(CallbackInfoReturnable<List<TagCompiler>> cir) {
        cir.setReturnValue(List.of(new DivTagCompiler(), new ATagCompiler(), new ColorTagCompiler(),
                new ItemLinkCompiler(), new FloatingImageCompiler(), new BreakCompiler(), new RecipeCompiler(),
                new ItemGridCompiler(), new CategoryIndexCompiler(), new BlockImageTagCompiler(),
                new ItemImageTagCompiler(), new BoxTagCompiler(BoxFlowDirection.ROW), new FluidLinkCompiler(),
                new BoxTagCompiler(BoxFlowDirection.COLUMN), new SceneTagCompiler(), new SubPagesCompiler(),
                new CommandLinkCompiler(), new PlayerNameTagCompiler(), new KeyBindTagCompiler(), new LatexCompiler()));
    }
}
