package com.hayden.acp_cdc_ai.repository;

import com.fasterxml.jackson.annotation.JsonIgnore;import com.hayden.multiagentidelib.model.worktree.WorktreeSandboxContext;
import com.hayden.utilitymodule.stream.StreamUtil;import lombok.Builder;
import lombok.With;

import java.nio.file.Path;import java.util.HashMap;
import java.util.List;import java.util.Map;import java.util.Optional;import java.util.stream.Stream;

@With
@Builder(toBuilder = true)
public record RequestContext(
        String sessionId,
        WorktreeSandboxContext sandboxContext,
        Map<String, String> metadata
) {
    public RequestContext {
        if (metadata == null) {
            metadata = new HashMap<>();
        }
    }

    @JsonIgnore
    public Path mainWorktreePath() {
        return Optional.ofNullable(sandboxContext)
                .flatMap(ws -> Optional.ofNullable(ws.mainWorktree()))
                .flatMap(ws -> Optional.ofNullable(ws.worktreePath()))
                .orElse(null);
    }

    @JsonIgnore
    public List<Path> submoduleWorktreePaths() {
        return Optional.ofNullable(sandboxContext)
                .stream()
                .flatMap(ws -> StreamUtil.toStream(ws.submoduleWorktrees()))
                .flatMap(ws -> Stream.ofNullable(ws.worktreePath()))
                .toList();
    }
}
